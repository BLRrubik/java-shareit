package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long id;
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime start;
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime end;
    private Item item;
    private User booker;
    private BookingStatus status;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private Long id;
        private String name;
    }
}
