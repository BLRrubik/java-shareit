package ru.practicum.shareit.item;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    private Item item;

    private User user;

    @BeforeEach
    void inti() {
        item = new Item();

        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setRequest(null);

        user = new User();
        user.setEmail("mail@mail.ru");
        user.setName("name");
    }

    @Test
    @Order(1)
    @Rollback(value = false)
    void saveItem(){

        userRepository.save(user);

        item.setOwner(user);

        itemRepository.save(item);

        Assertions.assertEquals(1L, item.getId());
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    void findAllByOwner() {
        List<Item> items = itemRepository.findAllByOwner(userRepository.findById(1L).get(),
                PageRequest.of(0,5, Sort.Direction.ASC,"id")).getContent();

        Assertions.assertEquals(1L, items.get(0).getId());
        Assertions.assertEquals(1, items.size());
    }

    @Test
    @Order(3)
    @Rollback()
    void searchTest() {
        List<Item> items = itemRepository.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(
                "name",
                "name",
                PageRequest.of(0,5, Sort.Direction.ASC, "id"))
                .getContent();

        Assertions.assertEquals(1L, items.get(0).getId());
        Assertions.assertEquals(1, items.size());
    }
}
