package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.excepton.UserAlreadyExistsException;
import ru.practicum.shareit.user.excepton.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.request.UserCreateRequest;
import ru.practicum.shareit.user.request.UserUpdateRequest;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private List<User> users;
    private Long counter;

    @Autowired
    public UserServiceImpl() {
        users = new ArrayList<>();
        counter = 1L;
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public User findById(Long userId) {
        if (users.stream().noneMatch(u -> u.getId().equals(userId))) {
            throw new UserNotFoundException("User with id: "+ userId +" is not found");
        }

        return users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .get();
    }

    @Override
    public User createUser(UserCreateRequest request) {
        if (users.stream().anyMatch(u -> u.getEmail().equals(request.getEmail()))) {
            throw new UserAlreadyExistsException("User with email: "+ request.getEmail() + " is already exists");
        }

        User user = new User();

        user.setId(counter++);
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        users.add(user);

        return user;
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        if (users.stream().anyMatch(u -> u.getEmail().equals(request.getEmail()))) {
            throw new UserAlreadyExistsException("User with email: "+ request.getEmail() + " is already exists");
        }

        User user = findById(userId);

        user.setName(request.getName() != null ? request.getName() : user.getName());
        user.setEmail(request.getEmail() != null ? request.getEmail() : user.getEmail());

        return user;
    }

    @Override
    public void deleteUser(Long userId) {
        User user = findById(userId);

        users.remove(user);
    }
}
