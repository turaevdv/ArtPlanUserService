package ru.turaev.userservice.exception.exceptionmodel;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class BaseExceptionModel {
    private String message;
    private HttpStatus status;
    private LocalDateTime localDateTime;
}
