package ru.practicum.shareit.requests.repositiry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequester(User requester);

    @Query(
            value = "SELECT * FROM item_requests " +
                    "WHERE requester_id != ?",
            nativeQuery = true
    )
    Page<ItemRequest> findAll(Long requesterId, Pageable pageable);
}
