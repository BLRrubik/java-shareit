package ru.practicum.shareit.booking.exceptions;

public class BookingUnsupportedTypeException extends RuntimeException {
    public BookingUnsupportedTypeException() {
    }

    public BookingUnsupportedTypeException(String message) {
        super(message);
    }
}
