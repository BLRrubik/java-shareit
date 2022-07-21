package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepo;
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
import ru.practicum.shareit.item.repository.CommentRepo;
import ru.practicum.shareit.item.repository.ItemRepo;
import ru.practicum.shareit.item.request.ItemCreateRequest;
import ru.practicum.shareit.item.request.ItemUpdateRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepo itemRepo;
    private final UserService userService;
    private final BookingRepo bookingRepo;
    private final CommentRepo commentRepo;

    @Autowired
    public ItemServiceImpl(UserService userService, ItemRepo itemRepo, BookingRepo bookingRepo, CommentRepo commentRepo) {
        this.userService = userService;
        this.itemRepo = itemRepo;
        this.bookingRepo = bookingRepo;
        this.commentRepo = commentRepo;
    }

    @Override
    public List<ItemDto> getAll(Long userPrincipal) {
        List<ItemDto> itemDtos = ItemMapper.toDtos(itemRepo.findAll());

        return setLastAndNextBookingForList(itemDtos, userPrincipal);
    }

    @Override
    public ItemDto findById(Long itemId, Long userPrincipal) {
        Item item = getItemById(itemId);

        ItemDto itemDto = ItemMapper.toDto(itemRepo.save(item));

        return setLastAndNextBooking(itemDto, userPrincipal);
    }

    @Override
    public Item getItemById(Long itemId) {
        if (!itemRepo.findById(itemId).isPresent()) {
            throw new ItemNotFoundException("item with id: "+ itemId +" not found");
        }

        Item item = itemRepo.findById(itemId).get();

        return item;
    }

    @Override
    public List<ItemDto> getItemOfUser(Long userPrincipal) {
        User user = userService.findById(userPrincipal);

        List<Item> items = itemRepo.findAllByOwner(user);

        List<ItemDto> itemDtos = ItemMapper.toDtos(items);

        return setLastAndNextBookingForList(itemDtos, userPrincipal);
    }

    @Override
    public ItemDto addItem(ItemCreateRequest request, Long userPrincipal) {
        User user = userService.findById(userPrincipal);

        //todo дальше здесь будет проверка на существование реквеста

        Item item = new Item();

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setAvailable(request.getAvailable());
        item.setOwner(user);

        itemRepo.save(item);

        ItemDto itemDto = ItemMapper.toDto(itemRepo.save(item));

        return setLastAndNextBooking(itemDto, userPrincipal);
    }

    @Override
    public ItemDto updateItem(ItemUpdateRequest request, Long itemId, Long userPrincipal) {
        User user = userService.findById(userPrincipal);

        Item item = getItemById(itemId);

        if (!item.getOwner().getId().equals(user.getId())) {
            throw new ItemOwnerException("user with id: "+ user.getId() +" is not owner of item with id: " + itemId);
        }

        item.setName(request.getName() != null ? request.getName() : item.getName());
        item.setDescription(request.getDescription() != null ? request.getDescription() : item.getDescription());
        item.setAvailable(request.getAvailable() != null ? request.getAvailable() : item.isAvailable());

        itemRepo.save(item);

        ItemDto itemDto = ItemMapper.toDto(itemRepo.save(item));

        return setLastAndNextBooking(itemDto, userPrincipal);
    }

    @Override
    public List<ItemDto> search(String text, Long userPrincipal) {
        if (text.isEmpty()) {
            return List.of();
        }

        List<ItemDto> itemDtos = ItemMapper.toDtos(
                itemRepo.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text)
        );

        return setLastAndNextBookingForList(itemDtos, userPrincipal);
    }

    @Override
    public CommentDto addComment(Long itemId, CommentDto request, Long userPrincipal) {
        Item item = getItemById(itemId);

        User user = userService.findById(userPrincipal);

        if (userService.checkUserBookings(user.getId(), item.getId())==null) {
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

        return CommentMapper.toDto(commentRepo.save(comment));
    }

    private ItemDto setLastAndNextBooking(ItemDto itemDto, Long userPrincipal) {

        Booking lastBooking;
        Booking nextBooking;

        if (!itemDto.getOwner().getId().equals(userPrincipal)) {
            nextBooking = null;
            lastBooking = null;
        }
        else {
            lastBooking = bookingRepo.findLastBooking(itemDto.getOwner().getId());
            nextBooking = bookingRepo.findNextBooking(itemDto.getOwner().getId());
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
