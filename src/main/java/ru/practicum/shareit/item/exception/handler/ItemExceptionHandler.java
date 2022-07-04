package ru.practicum.shareit.item.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.exception.Exception;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemOwnerException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ItemExceptionHandler {
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<Exception> itemNotFound(ItemNotFoundException e) {
        return new ResponseEntity<>(new Exception(e.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemOwnerException.class)
    public ResponseEntity<Exception> itemOwnerError(ItemOwnerException e) {
        return new ResponseEntity<>(new Exception(e.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }
}
