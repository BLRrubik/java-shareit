package ru.practicum.shareit.booking.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.booking.exceptions.BookingAccessException;
import ru.practicum.shareit.booking.exceptions.BookingNotFoundException;
import ru.practicum.shareit.booking.exceptions.BookingValidateException;
import ru.practicum.shareit.exception.ExceptionDTO;

import java.time.LocalDateTime;

@ControllerAdvice
public class BookingExceptionHandler {
    @ExceptionHandler(BookingAccessException.class)
    public ResponseEntity<ExceptionDTO> accessError(BookingAccessException e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingValidateException.class)
    public ResponseEntity<ExceptionDTO> validateError(BookingValidateException e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ExceptionDTO> notFound(BookingNotFoundException e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }
}
