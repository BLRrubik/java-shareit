package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemCommentValidationException;
import ru.practicum.shareit.item.exception.ItemDontHaveBookingForUserException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemOwnerException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.request.ItemCreateRequest;
import ru.practicum.shareit.item.request.ItemUpdateRequest;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemServiceImpl(UserService userService, ItemRepository itemRepository, BookingRepository bookingRepository, CommentRepository commentRepository, ItemRequestService itemRequestService) {
        this.userService = userService;
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.itemRequestService = itemRequestService;
    }

    @Override
    public List<ItemDto> getAll(Long userPrincipal) {
        List<ItemDto> itemDtos = ItemMapper.toDtos(itemRepository.findAll());

        return setLastAndNextBookingForList(itemDtos, userPrincipal);
    }

    @Override
    public ItemDto findById(Long itemId, Long userPrincipal) {
        Item item = getItemById(itemId);

        ItemDto itemDto = ItemMapper.toDto(itemRepository.save(item));

        return setLastAndNextBooking(itemDto, userPrincipal);
    }

    @Override
    public Item getItemById(Long itemId) {
        if (!itemRepository.findById(itemId).isPresent()) {
            throw new ItemNotFoundException("item with id: " + itemId + " not found");
        }

        Item item = itemRepository.findById(itemId).get();

        return item;
    }

    @Override
    public Page<ItemDto> getItemOfUser(Integer from, Integer size, Long userPrincipal) {
        User user = userService.findById(userPrincipal);

        Page<Item> page = itemRepository.findAllByOwner(
                user,
                PageRequest.of(
                        from,
                        size,
                        Sort.Direction.ASC, "created_at"
                ));

        List<Item> items = page.getContent();

        List<ItemDto> itemDtos = ItemMapper.toDtos(items);

        setLastAndNextBookingForList(itemDtos, userPrincipal);

        return new PageImpl<>(ItemMapper.toDtos(page.getContent()), page.getPageable(), page.getTotalElements());
    }

    @Override
    public ItemDto addItem(ItemCreateRequest request, Long userPrincipal) {
        User user = userService.findById(userPrincipal);

        ItemRequest itemRequest = null;

        if (request.getRequestId() != null) {
            itemRequest = itemRequestService.findById(request.getRequestId());
        }

        Item item = new Item();

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setAvailable(request.getAvailable());
        item.setOwner(user);
        item.setRequest(itemRequest);

        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(ItemUpdateRequest request, Long itemId, Long userPrincipal) {
        User user = userService.findById(userPrincipal);

        Item item = getItemById(itemId);

        if (!item.getOwner().getId().equals(user.getId())) {
            throw new ItemOwnerException("user with id: " + user.getId() + " is not owner of item with id: " + itemId);
        }

        ItemRequest itemRequest = null;

        if (request.getRequestId() != null) {
            itemRequest = itemRequestService.findById(request.getRequestId());
        }

        item.setName(request.getName() != null ? request.getName() : item.getName());
        item.setDescription(request.getDescription() != null ? request.getDescription() : item.getDescription());
        item.setAvailable(request.getAvailable() != null ? request.getAvailable() : item.isAvailable());
        item.setRequest(itemRequest != null ? itemRequest : item.getRequest());

        itemRepository.save(item);

        ItemDto itemDto = ItemMapper.toDto(itemRepository.save(item));

        return setLastAndNextBooking(itemDto, userPrincipal);
    }

    @Override
    public Page<ItemDto> search(String text, Integer from, Integer size, Long userPrincipal) {
        if (text.isEmpty()) {
            return Page.empty();
        }

        Page<Item> page =
                itemRepository.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text,
                        text,
                        PageRequest.of(
                                from,
                                size,
                                Sort.Direction.ASC,
                                "id")
                );

        List<ItemDto> itemDtos = ItemMapper.toDtos(page.getContent());

        setLastAndNextBookingForList(itemDtos, userPrincipal);

        return new PageImpl<>(ItemMapper.toDtos(page.getContent()), page.getPageable(), page.getTotalElements());
    }

    @Override
    public CommentDto addComment(Long itemId, CommentDto request, Long userPrincipal) {
        Item item = getItemById(itemId);

        User user = userService.findById(userPrincipal);

        if (userService.checkUserBookings(user.getId(), item.getId()) == null) {
            throw new ItemDontHaveBookingForUserException("User with id " + user.getId() +
                    " dont have any bookings for item with id " + item.getId());
        }

        if (request.getText().isEmpty()) {
            throw new ItemCommentValidationException("Comment should not be empty");
        }

        Comment comment = new Comment();

        comment.setItem(item);
        comment.setAuthor(user);
        comment.setText(request.getText());
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.toDto(commentRepository.save(comment));
    }

    private ItemDto setLastAndNextBooking(ItemDto itemDto, Long userPrincipal) {

        Booking lastBooking;
        Booking nextBooking;

        if (!itemDto.getOwner().getId().equals(userPrincipal)) {
            nextBooking = null;
            lastBooking = null;
        } else {
            lastBooking = bookingRepository.findLastBooking(itemDto.getOwner().getId(), itemDto.getId());
            nextBooking = bookingRepository.findNextBooking(itemDto.getOwner().getId(), itemDto.getId());
        }


        itemDto.setLastBooking(lastBooking == null ? null :
                new ItemDto.Booking(lastBooking.getId(), lastBooking.getBooker().getId()));
        itemDto.setNextBooking(nextBooking == null ? null :
                new ItemDto.Booking(nextBooking.getId(), nextBooking.getBooker().getId()));

        return itemDto;
    }

    private List<ItemDto> setLastAndNextBookingForList(List<ItemDto> itemDtos, Long userPrincipal) {
        return itemDtos.stream()
                .map(dto -> setLastAndNextBooking(dto, userPrincipal))
                .collect(Collectors.toList());
    }

}
