package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = "select * from users as u\n" +
            "left join bookings b on u.id = b.booker_id\n" +
            "left join items i on b.item_id = i.id\n" +
            "where u.id = ? and i.id = ? and b.end_date < now();", nativeQuery = true)
    User checkUserBooking(Long userId, Long itemId);
}
