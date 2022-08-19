package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.request.UserCreateRequest;
import ru.practicum.shareit.user.request.UserUpdateRequest;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User findById(Long id);

    User createUser(UserCreateRequest request);

    User updateUser(UserUpdateRequest request, Long userId);

    void deleteUser(Long userId);

    User checkUserBookings(Long userId, Long itemId);
}
