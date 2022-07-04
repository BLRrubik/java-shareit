package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto toDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                UserMapper.toDto(item.getOwner())
        );
    }

    public static List<ItemDto> toDtos(List<Item> items) {
        return items.stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }
}
