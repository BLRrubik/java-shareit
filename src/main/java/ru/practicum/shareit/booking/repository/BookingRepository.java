package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByBookerOrderByStartDesc(User booker, Pageable pageable);

    Page<Booking> findAllByBookerAndEndIsBeforeOrderByStartDesc(User booker, LocalDateTime now, Pageable pageable);

    Page<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(User booker, LocalDateTime now, Pageable pageable);

    Page<Booking> findAllByBookerAndStatusIsOrderByStartDesc(User booker, BookingStatus status, Pageable pageable);

    @Query(value = "select *\n" +
            "from bookings\n" +
            "where booker_id = ? and now() between start_date and end_date", nativeQuery = true)
    Page<Booking> getByCurrentStatus(Long bookerId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM bookings as b " +
            "LEFT JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.owner_id = ? " +
            "ORDER BY b.start_date DESC")
    Page<Booking> findForOwner(Long ownerId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM bookings as b " +
            "LEFT JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.owner_id = ? and now() between start_date and end_date " +
            "ORDER BY b.start_date DESC")
    Page<Booking> findForOwnerCurrent(Long ownerId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM bookings as b " +
            "LEFT JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.owner_id = ? AND b.end_date < now() " +
            "ORDER BY b.start_date DESC")
    Page<Booking> findForOwnerPast(Long ownerId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM bookings as b " +
            "LEFT JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.owner_id = ? AND b.start_date > now()" +
            "ORDER BY b.start_date DESC")
    Page<Booking> findForOwnerFuture(Long ownerId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM bookings as b " +
            "LEFT JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.owner_id = ? AND b.status = ? " +
            "ORDER BY b.start_date DESC")
    Page<Booking> findForOwnerByStatus(Long ownerId, String status, Pageable pageable);


    @Query(value = "select * from bookings as b\n" +
            "join items i on i.id = b.item_id\n" +
            "where i.owner_id = ? and i.id = ? and b.end_date < now() and b.status like 'APPROVED'\n" +
            "order by b.end_date desc\n" +
            "limit 1", nativeQuery = true)
    Booking findLastBooking(Long ownerId, Long itemId);

    @Query(nativeQuery = true, value = "select * from bookings as b\n" +
            "                  join items i on i.id = b.item_id\n" +
            "where i.owner_id = ? and i.id = ? and b.start_date > now() and b.status like 'APPROVED'\n" +
            "order by b.end_date asc\n" +
            "limit 1")
    Booking findNextBooking(Long ownerId, Long itemId);
}
