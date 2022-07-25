package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.requests.BookingCreateRequest;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingCreateRequest request, Long userPrincipal);

    BookingDto approveBooking(Long bookingId, boolean approve, Long userPrincipal);

    BookingDto getByIdForBookerOrOwner(Long bookingId, Long userPrincipal);

    List<BookingDto> getBookingsByUser(Long userPrincipal, State state);

    List<BookingDto> getBookingsByOwner(Long userPrincipal, State state);
}
