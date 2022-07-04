package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.InMemoryItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {
    private final InMemoryItemStorage storage;

    @Autowired
    public ItemServiceImpl(InMemoryItemStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<Item> getAll() {
        return storage.getAll();
    }

    @Override
    public Item getItem(Long id) {
        return null;
    }

    @Override
    public Optional<Item> addItem(ItemDto request, String userPrincipal) {
        Item item = new Item();

        return Optional.empty();
    }

    @Override
    public Optional<Item> updateItem(ItemDto request, String userPrincipal) {
        return Optional.empty();
    }

    @Override
    public List<Item> search(String text) {
        return null;
    }
}
