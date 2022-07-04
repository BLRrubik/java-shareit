package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDate start;
    private LocalDate end;
    private Item item;
    private User booker;
    private BookingStatus status;
}
