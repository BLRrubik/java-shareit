package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.request.UserCreateRequest;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@Transactional
@SpringBootTest(
        properties = "db.name=shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void getAll() {
        User user = userService.createUser(new UserCreateRequest("1", "gmail@gmail.com"));

        List<User> users = userService.getAll();

        assertThat(users.size(), equalTo(1));
        assertThat(users.get(0).getId(), equalTo(user.getId()));
        assertThat(users.get(0).getName(), equalTo(user.getName()));
        assertThat(users.get(0).getEmail(), equalTo(user.getEmail()));
    }
}
