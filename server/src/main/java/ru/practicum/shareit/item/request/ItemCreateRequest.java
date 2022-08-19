package ru.practicum.shareit.item.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemCreateRequest {

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;

}
