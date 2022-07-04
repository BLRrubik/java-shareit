package ru.practicum.shareit.item.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemUpdateRequest {
    private String name;
    private String description;
    private Boolean available;
}
