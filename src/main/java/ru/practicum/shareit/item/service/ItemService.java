package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.request.ItemCreateRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.request.ItemUpdateRequest;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAll(Long userPrincipal);

    ItemDto findById(Long itemId, Long userPrincipal);

    Item getItemById(Long itemId);

    List<ItemDto> getItemOfUser(Long userId);

    ItemDto addItem(ItemCreateRequest request, Long userPrincipal);

    ItemDto updateItem(ItemUpdateRequest request, Long itemId, Long userPrincipal);

    List<ItemDto> search(String text, Long userPrincipal);

    CommentDto addComment(Long itemId, CommentDto request, Long userPrincipal);
}
