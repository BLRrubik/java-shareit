package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.request.ItemCreateRequest;
import ru.practicum.shareit.item.request.ItemUpdateRequest;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userPrincipal) {
        return ResponseEntity.of(Optional.of(itemService.getItemOfUser(userPrincipal)));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable("itemId") Long itemId,
                                               @RequestHeader("X-Sharer-User-Id") Long userPrincipal) {
        return ResponseEntity.of(Optional.of(itemService.findById(itemId, userPrincipal)));
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestBody @Valid ItemCreateRequest request,
                                              @RequestHeader("X-Sharer-User-Id") Long userPrincipal) {
        return ResponseEntity.of(Optional.of(itemService.addItem(request, userPrincipal)));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@RequestBody @Valid ItemUpdateRequest request,
                                              @PathVariable("itemId") Long itemId,
                                              @RequestHeader("X-Sharer-User-Id") Long userPrincipal) {
        return ResponseEntity.of(Optional.of(itemService.updateItem(request, itemId, userPrincipal)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestParam(value = "text", required = false) String text,
                                                     @RequestHeader("X-Sharer-User-Id") Long userPrincipal) {
        return ResponseEntity.of(Optional.of(itemService.search(text, userPrincipal)));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(@PathVariable("itemId") Long itemId,
                                                 @RequestBody CommentDto request,
                                                 @RequestHeader("X-Sharer-User-Id") Long userPrincipal) {
        return ResponseEntity.of(Optional.of(itemService.addComment(itemId, request, userPrincipal)));
    }
}
