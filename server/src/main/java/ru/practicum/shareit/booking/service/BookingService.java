package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.requests.BookingCreateRequest;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingCreateRequest request, Long userPrincipal);

    BookingDto approveBooking(Long bookingId, boolean approve, Long userPrincipal);

    BookingDto getByIdForBookerOrOwner(Long bookingId, Long userPrincipal);

    Page<BookingDto> getBookingsByUser(Long userPrincipal, String state, Integer from, Integer size);

    Page<BookingDto> getBookingsByOwner(Long userPrincipal, String state, Integer from, Integer size);
}
