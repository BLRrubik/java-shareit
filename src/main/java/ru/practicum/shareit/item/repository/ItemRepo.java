package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepo extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner(User owner);

    List<Item> findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(String searchInName,
                                                                              String searchInDescription);
}
