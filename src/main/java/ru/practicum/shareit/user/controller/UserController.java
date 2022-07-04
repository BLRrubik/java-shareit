package ru.practicum.shareit.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.request.UserCreateRequest;
import ru.practicum.shareit.user.request.UserUpdateRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.of(Optional.of(UserMapper.toDtos(userService.getAll())));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getById(@PathVariable("userId") Long userId) {
        return ResponseEntity.of(
                Optional.of(
                        UserMapper.toDto(
                                userService.findById(userId)
                        )
                )
        );
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserCreateRequest request) {
        return ResponseEntity.of(
                Optional.of(
                        UserMapper.toDto(
                                userService.createUser(request)
                        )
                )
        );
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") Long userId,
                                              @RequestBody @Valid UserUpdateRequest request) {
        return ResponseEntity.of(
                Optional.of(
                        UserMapper.toDto(
                                userService.updateUser(request, userId)
                        )
                )
        );
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
