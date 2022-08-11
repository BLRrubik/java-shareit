package ru.practicum.shareit.item.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {

    private BookingRepository bookingRepository;


    public static ItemDto toDto(Item item) {

        List<ItemDto.Comment> comments = item.getComments().stream()
                .map(comment -> new ItemDto.Comment(
                        comment.getId(),
                        comment.getText(),
                        comment.getAuthor().getName(),
                        comment.getCreated()
                ))
                .collect(Collectors.toList());


        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                new ItemDto.User(
                        item.getOwner().getId(),
                        item.getOwner().getName()
                ),
                item.getRequest() != null ? item.getRequest().getId() : null,
                comments
        );
    }

    public static List<ItemDto> toDtos(List<Item> items) {
        return items.stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Page<ItemDto> convertPageToDto(Page<Item> page) {
        if (page.isEmpty()) {
            return Page.empty();
        }

        return new PageImpl<>(toDtos(page.getContent()), page.getPageable(), page.getTotalElements());
    }
}
