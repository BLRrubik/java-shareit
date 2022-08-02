package ru.practicum.shareit.requests.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.request.ItemRequestCreate;
import ru.practicum.shareit.requests.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ResponseEntity<ItemRequestDto> createRequest(@RequestHeader("X-Sharer-User-Id")
                                                            Long userPrincipal,
                                                        @RequestBody @Valid ItemRequestCreate requestCreate){

        return ResponseEntity.of(Optional.of(itemRequestService.createRequest(requestCreate, userPrincipal)));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getByUser(@RequestHeader("X-Sharer-User-Id")
                                                              Long userPrincipal) {
        return ResponseEntity.of(Optional.of(itemRequestService.getRequestsByUser(userPrincipal)));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDto> getById(@RequestHeader("X-Sharer-User-Id")
                                                      Long userPrincipal,
                                                  @PathVariable("requestId") Long requestId) {
        return ResponseEntity.of(Optional.of(itemRequestService.getById(requestId, userPrincipal)));
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userPrincipal,
                                       @RequestParam(value = "from", required = false) @Positive Integer from,
                                       @RequestParam(value = "size", required = false) @Positive Integer size) {

        return itemRequestService.getAll(from, size, userPrincipal).getContent();
    }
}
