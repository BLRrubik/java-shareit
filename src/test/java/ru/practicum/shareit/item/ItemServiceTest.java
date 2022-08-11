package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.request.ItemCreateRequest;
import ru.practicum.shareit.item.service.ItemService;
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
public class ItemServiceTest {
    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Test
    void getUserItems() {
        User user = userService.createUser(new UserCreateRequest("user" , "user@mail.com"));

        ItemDto itemDto = itemService.addItem(new ItemCreateRequest("name",
                "description",
                true,
                null),
                user.getId());

        List<ItemDto> items = itemService.getItemOfUser(0,5, user.getId()).getContent();

        assertThat(items.size(), equalTo(1));
        assertThat(items.get(0).getId(), equalTo(itemDto.getId()));
        assertThat(items.get(0).getName(), equalTo(itemDto.getName()));
        assertThat(items.get(0).getDescription(), equalTo(itemDto.getDescription()));
        assertThat(items.get(0).getOwner().getId(), equalTo(itemDto.getOwner().getId()));
    }
}
