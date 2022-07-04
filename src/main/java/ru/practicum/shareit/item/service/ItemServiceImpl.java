package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.request.ItemCreateRequest;
import ru.practicum.shareit.item.request.ItemUpdateRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private List<Item> items;
    private Long counter;
    private final UserService userService;

    @Autowired
    public ItemServiceImpl(UserService userService) {
        this.userService = userService;
        items = new ArrayList<>();
        counter = 1L;
    }

    @Override
    public List<Item> getAll() {
        return items;
    }

    @Override
    public Item findById(Long itemId) {
        if (items.stream().noneMatch(i -> i.getId().equals(itemId))) {
            throw new ItemNotFoundException("item with id: "+ itemId +" not found");
        }

        return items.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .get();
    }

    @Override
    public List<Item> getItemOfUser(Long userPrincipal) {
        User user = userService.findById(userPrincipal);

        return items.stream()
                .filter(i -> i.getOwner().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Item addItem(ItemCreateRequest request, Long userPrincipal) {
        User user = userService.findById(userPrincipal);

        Item item = new Item();

        item.setId(counter++);
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setAvailable(request.getAvailable());
        item.setOwner(user);

        items.add(item);

        return item;
    }

    @Override
    public Item updateItem(ItemUpdateRequest request, Long itemId, Long userPrincipal) {
        User user = userService.findById(userPrincipal);

        Item item = findById(itemId);

        if (!item.getOwner().getId().equals(user.getId())) {
            throw new ItemOwnerException("user with id: "+ user.getId() +" is not owner of item with id: " + itemId);
        }

        item.setName(request.getName() != null ? request.getName() : item.getName());
        item.setDescription(request.getDescription() != null ? request.getDescription() : item.getDescription());
        item.setAvailable(request.getAvailable() != null ? request.getAvailable() : item.isAvailable());

        return item;
    }

    @Override
    public List<Item> search(String text) {
        if (text.isEmpty()) {
            return List.of();
        }

        return items.stream()
                .filter(i -> i.getDescription().toLowerCase().contains(text.toLowerCase())
                        || i.getName().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::isAvailable)
                .collect(Collectors.toList());
    }
}
