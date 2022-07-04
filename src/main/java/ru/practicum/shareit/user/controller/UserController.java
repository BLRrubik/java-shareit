package ru.practicum.shareit.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.of(Optional.of(userService.getAll()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getById(@PathVariable("userId") Long userId) {
        return ResponseEntity.of(userService.findById(userId));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid UserDto request) {
        return ResponseEntity.of(userService.createUser(request));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") Long userId,
                                           @RequestBody @Valid UserUpdateDto request) {
        return ResponseEntity.of(userService.updateUser(request, userId));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
