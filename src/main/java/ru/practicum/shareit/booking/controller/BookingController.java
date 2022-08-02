package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.requests.BookingCreateRequest;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;
import java.util.Optional;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody BookingCreateRequest request,
                                                    @RequestHeader("X-Sharer-User-Id") Long userPrincipal) {
        return ResponseEntity.of(Optional.of(bookingService.createBooking(request, userPrincipal)));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> approveBooking(@PathVariable("bookingId") Long bookingId,
                                                     @RequestParam(value = "approved", required = true) boolean approve,
                                                     @RequestHeader("X-Sharer-User-Id") Long userPrincipal) {

        return ResponseEntity.of(Optional.of(bookingService.approveBooking(bookingId, approve, userPrincipal)));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getByIdForOwnerOfItemOrBooking(@PathVariable("bookingId") Long bookingId,
                                                                     @RequestHeader("X-Sharer-User-Id")
                                                                     Long userPrincipal) {

        return ResponseEntity.of(Optional.of(bookingService.getByIdForBookerOrOwner(bookingId, userPrincipal)));
    }

    @GetMapping()
    public ResponseEntity<List<BookingDto>> getBookingsByCurrentUser(@RequestParam(value = "state", required = false,
            defaultValue = "ALL") String state,
                                                                     @RequestHeader("X-Sharer-User-Id")
                                                                     Long userPrincipal) {
        return ResponseEntity.of(Optional.of(bookingService.getBookingsByUser(userPrincipal, state)));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getBookingsByOwner(@RequestParam(value = "state", required = false,
            defaultValue = "ALL") String state,
                                                               @RequestHeader("X-Sharer-User-Id") Long userPrincipal) {
        return ResponseEntity.of(Optional.of(bookingService.getBookingsByOwner(userPrincipal, state)));
    }
}
