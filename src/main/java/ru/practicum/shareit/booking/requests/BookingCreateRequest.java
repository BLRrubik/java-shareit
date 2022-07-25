package ru.practicum.shareit.booking.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateRequest {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
}
