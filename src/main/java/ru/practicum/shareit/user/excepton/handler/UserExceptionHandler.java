package ru.practicum.shareit.user.excepton.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.exception.Exception;
import ru.practicum.shareit.user.excepton.UserAlreadyExistsException;
import ru.practicum.shareit.user.excepton.UserNotFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Exception> userNotFound(UserNotFoundException e) {
        return new ResponseEntity<>(new Exception(e.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Exception> userAlreadyExists(UserAlreadyExistsException e) {
        return new ResponseEntity<>(new Exception(e.getMessage(), LocalDateTime.now()), HttpStatus.CONFLICT);
    }

}
