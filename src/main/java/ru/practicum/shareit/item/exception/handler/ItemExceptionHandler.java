package ru.practicum.shareit.item.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.exception.ExceptionDTO;
import ru.practicum.shareit.item.exception.ItemCommentValidationException;
import ru.practicum.shareit.item.exception.ItemDontHaveBookingForUserException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemOwnerException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ItemExceptionHandler {
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ExceptionDTO> itemNotFound(ItemNotFoundException e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemOwnerException.class)
    public ResponseEntity<ExceptionDTO> itemOwnerError(ItemOwnerException e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemDontHaveBookingForUserException.class)
    public ResponseEntity<ExceptionDTO> userDontHaveBooking(ItemDontHaveBookingForUserException e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemCommentValidationException.class)
    public ResponseEntity<ExceptionDTO> validationError(ItemCommentValidationException e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
}
