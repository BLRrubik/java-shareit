package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.request.UserCreateRequest;
import ru.practicum.shareit.user.request.UserUpdateRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public Object getAll() {
        return userClient.getAll();
    }

    @PostMapping
    public Object createUser(@RequestBody @Valid UserCreateRequest request) {
        log.info("Creating user {}", request.toString());

        return userClient.createUser(request);
    }

    @GetMapping("/{userId}")
    public Object getById(@PathVariable("userId") @Positive Long userId) {
        return userClient.getById(userId);
    }

    @PatchMapping("/{userId}")
    public Object updateUser(@PathVariable("userId") @Positive Long userId,
                             @RequestBody @Valid UserUpdateRequest request) {
        return userClient.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userClient.deleteUser(userId);
    }
}
