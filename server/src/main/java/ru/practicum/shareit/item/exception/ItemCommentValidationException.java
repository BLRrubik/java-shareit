package ru.practicum.shareit.item.exception;

public class ItemCommentValidationException extends RuntimeException {
    public ItemCommentValidationException() {
    }

    public ItemCommentValidationException(String message) {
        super(message);
    }
}
