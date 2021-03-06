package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerOrderByStartDesc(User booker);

    List<Booking> findAllByBookerAndEndIsBeforeOrderByStartDesc(User booker, LocalDateTime now);

    List<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(User booker, LocalDateTime now);

    List<Booking> findAllByBookerAndStatusIsOrderByStartDesc(User booker, BookingStatus status);

    @Query(nativeQuery = true, value = "SELECT * FROM bookings as b " +
            "LEFT JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.owner_id = ? " +
            "ORDER BY b.start_date DESC")
    List<Booking> findForOwner(Long ownerId);

    @Query(value = "select * from bookings as b\n" +
            "join items i on i.id = b.item_id\n" +
            "where i.owner_id = ? and b.end_date < now() and b.status like 'APPROVED'\n" +
            "order by b.end_date desc\n" +
            "limit 1", nativeQuery = true)
    Booking findLastBooking(Long ownerId);

    @Query(nativeQuery = true, value = "select * from bookings as b\n" +
            "                  join items i on i.id = b.item_id\n" +
            "where i.owner_id = ? and b.start_date > now() and b.status like 'APPROVED'\n" +
            "order by b.end_date asc\n" +
            "limit 1")
    Booking findNextBooking(Long ownerId);
}
