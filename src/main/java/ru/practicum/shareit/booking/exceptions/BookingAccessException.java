package ru.practicum.shareit.booking.exceptions;

public class BookingAccessException extends RuntimeException{
    public BookingAccessException() {
    }

    public BookingAccessException(String message) {
        super(message);
    }
}
