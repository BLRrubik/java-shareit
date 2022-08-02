package ru.practicum.shareit.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.requests.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.requests.repositiry.ItemRequestRepository;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.request.ItemRequestCreate;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemRequestServiceImpl implements ItemRequestService{
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserService userService) {
        this.itemRequestRepository = itemRequestRepository;
        this.userService = userService;
    }

    @Override
    public ItemRequestDto createRequest(ItemRequestCreate requestCreate, Long userPrincipal) {
        User requester = userService.findById(userPrincipal);

        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setDescription(requestCreate.getDescription());
        itemRequest.setRequester(requester);
        itemRequest.setCreated(LocalDateTime.now());

        return ItemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public Page<ItemRequestDto> getAll(Integer from, Integer size, Long userPrincipal) {

        User user = userService.findById(userPrincipal);

        if (size == null || from == null) {
            return Page.empty();
        }

        return ItemRequestMapper.convertPageToDto(itemRequestRepository.findAll(
                user.getId(),
                PageRequest.of(
                from,
                size,
                Sort.Direction.ASC, "created_at"
        )));
    }

    @Override
    public ItemRequestDto getById(Long requestId, Long userPrincipal) {
        User user = userService.findById(userPrincipal);

        return ItemRequestMapper.toDto(findById(requestId));
    }

    @Override
    public ItemRequest findById(Long requestId) {
        if (!itemRequestRepository.findById(requestId).isPresent()) {
            throw new ItemRequestNotFoundException("Item request with id " + requestId + " is not found");
        }

        return itemRequestRepository.findById(requestId).get();
    }

    @Override
    public List<ItemRequestDto> getRequestsByUser(Long userPrincipal) {
        User requester = userService.findById(userPrincipal);

        return ItemRequestMapper.toDtos(itemRequestRepository.findAllByRequester(requester));
    }
}
