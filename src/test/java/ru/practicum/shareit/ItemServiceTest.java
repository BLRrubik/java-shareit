package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.request.ItemCreateRequest;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ItemServiceTest {
    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private ItemDto itemDto;

    @Mock
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .build();

        itemDto = new ItemDto(
                1L,
                "name",
                "description",
                true,
                new ItemDto.User(
                        1L,
                        "username"
                ),
                1L,
                new ArrayList<>()
        );

        user = new User(
                1L,
                "username",
                "email"
        );
    }

    @Test
    void createItem() throws Exception{
        when(userService.createUser(any()))
                .thenReturn(user);

        when(itemService.addItem(any(), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.email", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.comments",hasSize(0)));
    }
}
