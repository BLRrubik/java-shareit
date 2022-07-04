package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryItemStorage {
    private List<Item> items = new ArrayList<>();

    public List<Item> getAll() {
        return items;
    }

    public Item addItem(Item item){
        items.add(item);
        return item;
    }

    public Item getById(Long id) {
        if (items.stream()
                .noneMatch(i -> i.getId().equals(id))) {
            //todo item is not found
            return null;
        }

        return items.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst().get();
    }
}
