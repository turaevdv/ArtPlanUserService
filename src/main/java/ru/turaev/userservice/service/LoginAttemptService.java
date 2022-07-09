package ru.turaev.userservice.service;

import ru.turaev.userservice.entity.User;

public interface LoginAttemptService {
    void saveAttempt(User user);

    int numberOfUserAttempt(User user);

    void deleteUserAttempts(User user);
}
