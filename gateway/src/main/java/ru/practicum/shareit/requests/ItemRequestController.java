package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.request.ItemRequestCreate;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public Object createRequest(@RequestHeader("X-Sharer-User-Id") @Positive Long userPrincipal,
                                @RequestBody @Valid ItemRequestCreate requestCreate) {
        log.info("Creating item request {} by user={}", requestCreate, userPrincipal);

        return itemRequestClient.createRequest(userPrincipal, requestCreate);
    }

    @GetMapping
    public Object getByUser(@RequestHeader("X-Sharer-User-Id") @Positive Long userPrincipal) {
        log.info("Getting item requests by user={}", userPrincipal);

        return itemRequestClient.getByUser(userPrincipal);
    }

    @GetMapping("/{requestId}")
    public Object getById(@RequestHeader("X-Sharer-User-Id") @Positive Long userPrincipal,
                          @PathVariable("requestId") @Positive Long requestId) {
        log.info("Getting item request {} by user={}", requestId, userPrincipal);

        return itemRequestClient.getById(userPrincipal, requestId);
    }

    @GetMapping("/all")
    public Object getAll(@RequestHeader("X-Sharer-User-Id") @Positive Long userPrincipal,
                         @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
                         @RequestParam(value = "size", required = false) @Positive Integer size) {
        log.info("Getting all item requests by user={}", userPrincipal);

        return itemRequestClient.getAll(userPrincipal, from, size);
    }
}
