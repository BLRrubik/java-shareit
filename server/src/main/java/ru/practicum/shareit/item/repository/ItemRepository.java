package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByOwner(User owner, Pageable pageable);

    Page<Item> findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(String searchInName,
                                                                              String searchInDescription,
                                                                              Pageable pageable);
}
