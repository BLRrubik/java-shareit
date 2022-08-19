package ru.practicum.shareit.requests.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {
    public static ItemRequestDto toDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                new ItemRequestDto.User(
                        itemRequest.getRequester().getId(),
                        itemRequest.getRequester().getName()
                ),
                itemRequest.getCreated(),
                itemRequest.getItems().stream()
                        .map(item -> new ItemRequestDto.Item(
                                item.getId(),
                                item.getName(),
                                item.getDescription(),
                                item.isAvailable(),
                                itemRequest.getId()
                        ))
                        .collect(Collectors.toList())

        );
    }

    public static List<ItemRequestDto> toDtos(List<ItemRequest> requests) {
        return requests.stream()
                .map(ItemRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Page<ItemRequestDto> convertPageToDto(Page<ItemRequest> page) {
        if (page.isEmpty()) {
            return Page.empty();
        }

        return new PageImpl<>(toDtos(page.getContent()), page.getPageable(), page.getTotalElements());
    }
}
