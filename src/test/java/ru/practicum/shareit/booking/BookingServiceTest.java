package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.requests.BookingCreateRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.request.ItemCreateRequest;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.request.UserCreateRequest;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Test
    void getOwnerBookings() {
        User userOwnerOfItem = userService.createUser(new UserCreateRequest("user", "user@mail.com"));
        User userBooker = userService.createUser(new UserCreateRequest("booker", "booker@mail.com"));

        ItemDto itemDto = itemService.addItem(new ItemCreateRequest("name",
                        "description",
                        true,
                        null),
                userOwnerOfItem.getId());

        BookingDto bookingDto = bookingService.createBooking(
                new BookingCreateRequest(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        itemDto.getId()
                ),
                userBooker.getId()
        );

        List<BookingDto> bookings = bookingService.getBookingsByOwner(userOwnerOfItem.getId(), "ALL", 0, 5)
                .getContent();

        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getId(), equalTo(bookingDto.getId()));
        assertThat(bookings.get(0).getStatus(), equalTo(bookingDto.getStatus()));
        assertThat(bookings.get(0).getStart(), equalTo(bookingDto.getStart()));
        assertThat(bookings.get(0).getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(bookings.get(0).getBooker().getId(), equalTo(bookingDto.getBooker().getId()));
        assertThat(bookings.get(0).getItem().getId(), equalTo(bookingDto.getItem().getId()));
    }
}