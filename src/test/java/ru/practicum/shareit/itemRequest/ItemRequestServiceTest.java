package ru.practicum.shareit.itemRequest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.request.ItemCreateRequest;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.request.ItemRequestCreate;
import ru.practicum.shareit.requests.service.ItemRequestService;
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
public class ItemRequestServiceTest {
    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserService userService;

    @Test
    void getAll() {
        User user = userService.createUser(new UserCreateRequest("jhon", "jhon@mail.ru"));
        User observer = userService.createUser(new UserCreateRequest("zack", "zack@mail.ru"));


        ItemRequestDto itemRequestDto = itemRequestService.createRequest(new ItemRequestCreate("description"),
                user.getId());


        List<ItemRequestDto> requests = itemRequestService.getAll(0, 5, observer.getId()).getContent();

        assertThat(requests.size(), equalTo(1));
        assertThat(requests.get(0).getId(), equalTo(itemRequestDto.getId()));
        assertThat(requests.get(0).getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(requests.get(0).getRequestor().getId(), equalTo(itemRequestDto.getRequestor().getId()));
        assertThat(requests.get(0).getCreated(), equalTo(itemRequestDto.getCreated()));
    }
}
