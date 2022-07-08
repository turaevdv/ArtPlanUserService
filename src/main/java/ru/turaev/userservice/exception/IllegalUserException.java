package ru.turaev.userservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import ru.turaev.userservice.dto.UserDto;

public class IllegalUserException extends IllegalException {
    @Getter
    @Setter
    private UserDto userDto;

    public IllegalUserException(String message, HttpStatus httpStatus, UserDto userDto) {
        super(message, httpStatus);
        this.userDto = userDto;
    }
}
