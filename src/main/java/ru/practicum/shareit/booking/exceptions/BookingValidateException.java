package ru.practicum.shareit.booking.exceptions;

public class BookingValidateException extends RuntimeException{
    public BookingValidateException() {
    }

    public BookingValidateException(String message) {
        super(message);
    }
}
