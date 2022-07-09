package ru.turaev.userservice.service;

import org.springframework.http.ResponseEntity;
import ru.turaev.userservice.dto.LoginViewModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface LoginService {
    ResponseEntity<Map<Object, Object>> authenticate(LoginViewModel model);
    boolean verifyToken(HttpServletRequest request);
}
