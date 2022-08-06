package ru.practicum.shareit.booking.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {
    public static BookingDto toDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                new BookingDto.Item(booking.getItem().getId(),
                        booking.getItem().getName()),
                new BookingDto.User(booking.getBooker().getId(),
                        booking.getBooker().getName()),
                booking.getStatus()
        );
    }

    public static List<BookingDto> toDtos(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Page<BookingDto> convertPageToDto(Page<Booking> page) {
        if (page.isEmpty()) {
            return Page.empty();
        }

        return new PageImpl<>(toDtos(page.getContent()), page.getPageable(), page.getTotalElements());
    }
}
