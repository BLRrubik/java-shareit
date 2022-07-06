package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto toDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                new ItemDto.User(
                        item.getOwner().getId(),
                        item.getOwner().getName()
                ),
                item.getRequest() != null ? new ItemDto.Request(item.getRequest().getId()) : null
        );
    }

    public static List<ItemDto> toDtos(List<Item> items) {
        return items.stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }
}
