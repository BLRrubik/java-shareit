package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping
	public Object getBookings(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
							  @RequestParam(name = "state", defaultValue = "all") String stateParam,
							  @RequestParam(name = "from", required = false) @PositiveOrZero Integer from,
							  @RequestParam(name = "size", required = false) @Positive Integer size) {
		BookingState state = BookingState
				.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@PostMapping
	public Object bookItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
			@RequestBody @Valid BookItemRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public Object getBooking(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
							 @PathVariable @Positive Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@GetMapping("/owner")
	public Object getBookingsByOwner(@RequestParam(value = "state", required = false, defaultValue = "ALL")
										 String state,
									 @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
									 @RequestParam(value = "size", required = false) @Positive Integer size,
									 @RequestHeader("X-Sharer-User-Id") @Positive Long userPrincipal) {
		log.info("Get bookings by owner userId={}", userPrincipal);

		return bookingClient.getOwnerBookings(userPrincipal, BookingState.valueOf(state), from, size);
	}

	@PatchMapping("/{bookingId}")
	public Object approveBooking(@PathVariable("bookingId") @Positive Long bookingId,
								 @RequestParam(value = "approved") boolean approve,
								 @RequestHeader("X-Sharer-User-Id") @Positive Long userPrincipal) {
		log.info("Approve booking {} by userId={}", bookingId, userPrincipal);

		System.out.println(approve);

		return bookingClient.approveBooking(bookingId, approve, userPrincipal);
	}
}
