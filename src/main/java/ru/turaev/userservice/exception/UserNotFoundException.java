package ru.turaev.userservice.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserNotFoundException extends RuntimeException {
    private LocalDateTime localDateTime;

    public UserNotFoundException(String message) {
        super(message);
        this.localDateTime = LocalDateTime.now();
    }
}
