package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.request.ItemCreateRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.request.ItemUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> getAll();

    Item findById(Long itemId);

    List<Item> getItemOfUser(Long userId);

    Item addItem(ItemCreateRequest request, Long userPrincipal);

    Item updateItem(ItemUpdateRequest request, Long itemId, Long userPrincipal);

    List<Item> search(String text);
}
