package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.request.ItemCreateRequest;
import ru.practicum.shareit.item.request.ItemUpdateRequest;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAll(Long userPrincipal);

    ItemDto findById(Long itemId, Long userPrincipal);

    Item getItemById(Long itemId);

    Page<ItemDto> getItemOfUser(Integer from, Integer size, Long userId);

    ItemDto addItem(ItemCreateRequest request, Long userPrincipal);

    ItemDto updateItem(ItemUpdateRequest request, Long itemId, Long userPrincipal);

    Page<ItemDto> search(String text, Integer from, Integer size, Long userPrincipal);

    CommentDto addComment(Long itemId, CommentDto request, Long userPrincipal);
}
