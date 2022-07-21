package ru.practicum.shareit.item.exception;

public class ItemDontHaveBookingForUserException extends RuntimeException{
    public ItemDontHaveBookingForUserException() {
    }

    public ItemDontHaveBookingForUserException(String message) {
        super(message);
    }
}
