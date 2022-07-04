package ru.practicum.shareit.item.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemCreateRequest {
    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty
    @NotNull
    private String description;

    @NotNull
    private Boolean available;

}
