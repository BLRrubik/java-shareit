package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exceptions.BookingAccessException;
import ru.practicum.shareit.booking.exceptions.BookingNotFoundException;
import ru.practicum.shareit.booking.exceptions.BookingUnsupportedTypeException;
import ru.practicum.shareit.booking.exceptions.BookingValidateException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.requests.BookingCreateRequest;
import ru.practicum.shareit.item.exception.ItemOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.excepton.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, ItemService itemService, UserService userService) {
        this.bookingRepository = bookingRepository;
        this.itemService = itemService;
        this.userService = userService;
    }

    @Override
    public BookingDto createBooking(BookingCreateRequest request, Long userPrincipal) {
        if (request.getEnd().isBefore(LocalDateTime.now())
                || request.getStart().isBefore(LocalDateTime.now())
                || request.getEnd().isBefore(request.getStart())) {
            throw new BookingValidateException("Date is not valid");
        }

        Item item = itemService.getItemById(request.getItemId());

        if (!item.isAvailable()) {
            throw new BookingValidateException("Item with id " + item.getId() + " is not available");
        }

        User booker = userService.findById(userPrincipal);

        if (item.getOwner().getId().equals(booker.getId())) {
            throw new UserNotFoundException("User with id " + booker.getId() +
                    " owner of item with id " + item.getId());
        }

        Booking booking = new Booking();

        booking.setStart(request.getStart());
        booking.setEnd(request.getEnd());
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto approveBooking(Long bookingId, boolean approve, Long userPrincipal) {
        if (!bookingRepository.findById(bookingId).isPresent()) {
            throw new BookingNotFoundException("Booking with id " + bookingId + " is not found");
        }

        Booking booking = bookingRepository.findById(bookingId).get();

        User ownerOfItem = userService.findById(userPrincipal);

        if (!booking.getItem().getOwner().getId().equals(ownerOfItem.getId())) {
            throw new ItemOwnerException("user with id: " + ownerOfItem.getId() + " is not owner " +
                    "of item with id: " + booking.getItem().getId());
        }

        if (booking.getStatus().equals(BookingStatus.APPROVED) && approve) {
            throw new BookingValidateException("Already approved");
        }

        BookingStatus status = approve ? BookingStatus.APPROVED : BookingStatus.REJECTED;

        booking.setStatus(status);

        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getByIdForBookerOrOwner(Long bookingId, Long userPrincipal) {
        if (!bookingRepository.findById(bookingId).isPresent()) {
            throw new BookingNotFoundException("Booking with id " + bookingId + " is not found");
        }

        Booking booking = bookingRepository.findById(bookingId).get();

        boolean state = !userPrincipal.equals(booking.getBooker().getId()) &&
                !userPrincipal.equals(booking.getItem().getOwner().getId());

        if (state) {
            throw new BookingAccessException("User with id " + userPrincipal + " is not owner of item or booking");
        }

        return BookingMapper.toDto(booking);
    }

    @Override
    public Page<BookingDto> getBookingsByUser(Long userPrincipal, String stringState, Integer from, Integer size) {
        User booker = userService.findById(userPrincipal);

        State state;

        try {
            state = State.valueOf(stringState);
        } catch (IllegalArgumentException e) {
            throw new BookingUnsupportedTypeException("Unknown state: UNSUPPORTED_STATUS");
        }

        if (from == null || size == null) {
            return Page.empty();
        }

        switch (state) {
            case PAST:
                return BookingMapper.convertPageToDto(bookingRepository.findAllByBookerAndEndIsBeforeOrderByStartDesc(
                        booker,
                        LocalDateTime.now(),
                        PageRequest.of(
                                from,
                                size,
                                Sort.Direction.ASC,
                                "id"
                        )));
            case FUTURE:
                return BookingMapper.convertPageToDto(
                        bookingRepository.findAllByBookerAndStartIsAfterOrderByStartDesc(booker,
                        LocalDateTime.now(), PageRequest.of(
                                        from,
                                        size,
                                        Sort.Direction.ASC,
                                        "id"
                                )));
            case WAITING:
                return BookingMapper.convertPageToDto(bookingRepository.findAllByBookerAndStatusIsOrderByStartDesc(booker,
                        BookingStatus.WAITING, PageRequest.of(
                                from,
                                size,
                                Sort.Direction.ASC,
                                "id"
                        )));
            case REJECTED:
                return BookingMapper.convertPageToDto(bookingRepository.findAllByBookerAndStatusIsOrderByStartDesc(
                        booker,
                        BookingStatus.REJECTED,
                        PageRequest.of(
                                from,
                                size,
                                Sort.Direction.ASC,
                                "id"
                        )));
            case CURRENT:
                return BookingMapper.convertPageToDto(bookingRepository.getByCurrentStatus(booker.getId(),
                        PageRequest.of(
                        from,
                        size,
                        Sort.Direction.ASC,
                        "id"
                )));
            default:
                return BookingMapper.convertPageToDto(bookingRepository.findAllByBookerOrderByStartDesc(booker,
                        PageRequest.of(
                        from,
                        size,
                        Sort.Direction.ASC,
                        "id"
                )));

        }
    }

    @Override
    public Page<BookingDto> getBookingsByOwner(Long userPrincipal, String stringState, Integer from, Integer size) {
        User owner = userService.findById(userPrincipal);

        State state;

        try {
            state = State.valueOf(stringState);
        } catch (IllegalArgumentException e) {
            throw new BookingUnsupportedTypeException("Unknown state: UNSUPPORTED_STATUS");
        }

        Page<Booking> bookings = bookingRepository.findForOwner(owner.getId(), PageRequest.of(
                from,
                size,
                Sort.Direction.ASC,
                "id"
        ));

        if (bookings.isEmpty()) {
            throw new BookingAccessException("Owner with id " + owner.getId() + " dont have any bookings");
        }

        switch (state) {
            case PAST:
                bookings = bookingRepository.findForOwnerPast(owner.getId(), PageRequest.of(
                        from,
                        size,
                        Sort.Direction.ASC,
                        "id"
                ));
                break;
            case FUTURE:
                bookings = bookingRepository.findForOwnerFuture(owner.getId(), PageRequest.of(
                        from,
                        size,
                        Sort.Direction.ASC,
                        "id"
                ));
                break;
            case WAITING:
                bookings = bookingRepository.findForOwnerByStatus(owner.getId(), BookingStatus.WAITING.toString(),
                        PageRequest.of(
                        from,
                        size,
                        Sort.Direction.ASC,
                        "id"
                ));
                break;
            case REJECTED:
                bookings = bookingRepository.findForOwnerByStatus(owner.getId(), BookingStatus.REJECTED.toString(),
                        PageRequest.of(
                        from,
                        size,
                        Sort.Direction.ASC,
                        "id"
                ));
                break;
            case CURRENT:
                bookings = bookingRepository.findForOwnerCurrent(owner.getId(), PageRequest.of(
                        from,
                        size,
                        Sort.Direction.ASC,
                        "id"
                ));
                break;
        }

        return BookingMapper.convertPageToDto(bookings);
    }

}
