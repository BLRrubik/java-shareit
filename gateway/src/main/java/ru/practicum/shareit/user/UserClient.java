package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.request.UserCreateRequest;
import ru.practicum.shareit.user.request.UserUpdateRequest;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public Object createUser(UserCreateRequest request) {
        return post("", 0, request);
    }

    public Object getAll() {
        return get("", 0);
    }

    public Object getById(Long userId) {
        return get("/" + userId, 0);
    }

    public Object updateUser(Long userId, UserUpdateRequest request) {
        return patch("/" + userId, 0, request);
    }

    public void deleteUser(Long userId) {
        delete("/" + userId, 0);
    }
}
