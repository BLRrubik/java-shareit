package ru.practicum.shareit.requests.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.exception.ExceptionDTO;
import ru.practicum.shareit.requests.exceptions.ItemRequestNotFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ItemRequestExceptionHandler {
    @ExceptionHandler(ItemRequestNotFoundException.class)
    public ResponseEntity<ExceptionDTO> requestNotFound(ItemRequestNotFoundException e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }
}
