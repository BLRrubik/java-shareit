package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();

    Optional<User> findById(Long id);

    Optional<User> createUser(UserDto request);

    Optional<User> updateUser(UserUpdateDto request, Long userId);

    void deleteUser(Long userId);
}
