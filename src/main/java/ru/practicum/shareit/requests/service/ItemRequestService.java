package ru.practicum.shareit.requests.service;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.request.ItemRequestCreate;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createRequest(ItemRequestCreate requestCreate, Long userPrincipal);

    Page<ItemRequestDto> getAll(Integer from, Integer size, Long userPrincipal);

    ItemRequestDto getById(Long requestId, Long userPrincipal);

    ItemRequest findById(Long requestId);

    List<ItemRequestDto> getRequestsByUser(Long userPrincipal);
}
