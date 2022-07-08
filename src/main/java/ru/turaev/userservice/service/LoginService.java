package ru.turaev.userservice.service;

import org.springframework.http.ResponseEntity;
import ru.turaev.userservice.dto.LoginViewModel;

public interface LoginService {
    ResponseEntity<?> authenticate(LoginViewModel model);
}
