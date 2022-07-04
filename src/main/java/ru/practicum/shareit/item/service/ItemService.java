package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> getAll();

    Item getItem(Long id);

    Optional <Item> addItem(ItemDto request, String userPrincipal);

    Optional <Item> updateItem(ItemDto request, String userPrincipal);

    List<Item> search(String text);
}
