package ru.turaev.userservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
    @Getter
    private HttpStatus httpStatus;

    public JwtAuthenticationException(String s) {
        super(s);
    }

    public JwtAuthenticationException(String s, HttpStatus httpStatus) {
        super(s);
        this.httpStatus = httpStatus;
    }
}
