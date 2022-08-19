package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.request.CommentDto;
import ru.practicum.shareit.item.request.ItemCreateRequest;
import ru.practicum.shareit.item.request.ItemUpdateRequest;

import java.util.HashMap;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public Object getItemsOfUser(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = null;

        if (from != null && size != null) {
            parameters = Map.of(
                    "from", from,
                    "size", size
            );
        }

        return get("", userId, parameters);
    }

    public Object getItemById(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public Object createItem(ItemCreateRequest request, Long userId) {
        return post("", userId, request);
    }

    public Object updateItem(ItemUpdateRequest request, Long itemId, Long userId) {
        return patch("/" + itemId, userId, request);
    }

    public Object search(String text, Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = new HashMap<>();

        if (from != null && size != null) {
            parameters.put("from", from);
            parameters.put("size", size);
        }

        parameters.put("text", text);

        return get("/search", userId, parameters);
    }

    public Object addComment(Long itemId, CommentDto commentDto, Long userId) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }

}
