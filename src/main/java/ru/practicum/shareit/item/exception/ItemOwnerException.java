package ru.practicum.shareit.item.exception;

public class ItemOwnerException extends RuntimeException {
    public ItemOwnerException() {
    }

    public ItemOwnerException(String message) {
        super(message);
    }
}
