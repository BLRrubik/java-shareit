package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.excepton.UserAlreadyExistsException;
import ru.practicum.shareit.user.excepton.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.request.UserCreateRequest;
import ru.practicum.shareit.user.request.UserUpdateRequest;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id: " + userId + " is not found");
        }

        return userRepository.findById(userId).get();
    }

    @Override
    public User createUser(UserCreateRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email: " + request.getEmail() + " is already exists");
        }

        User user = findById(userId);

        user.setName(request.getName() != null ? request.getName() : user.getName());
        user.setEmail(request.getEmail() != null ? request.getEmail() : user.getEmail());

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = findById(userId);

        userRepository.delete(user);
    }

    public User checkUserBookings(Long userId, Long itemId) {
        return userRepository.checkUserBooking(userId, itemId);
    }
}
