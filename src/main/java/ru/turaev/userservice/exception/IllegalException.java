package ru.turaev.userservice.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class IllegalException extends RuntimeException {
    private HttpStatus status;
    private LocalDateTime localDateTime;

    public IllegalException(String message, HttpStatus httpStatus) {
        super(message);
        this.status = httpStatus;
        this.localDateTime = LocalDateTime.now();
    }
}
