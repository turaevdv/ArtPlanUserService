package ru.turaev.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.turaev.userservice.dto.UserDto;
import ru.turaev.userservice.entity.User;
import ru.turaev.userservice.enums.Role;
import ru.turaev.userservice.exception.IllegalUserException;
import ru.turaev.userservice.exception.UserAlreadyExistException;
import ru.turaev.userservice.exception.UserNotFoundException;
import ru.turaev.userservice.mapper.UserMapper;
import ru.turaev.userservice.repository.UserRepository;
import ru.turaev.userservice.service.LoginAttemptService;
import ru.turaev.userservice.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LoginAttemptService loginAttemptService;

    private static final Supplier<UserNotFoundException> USER_NOT_FOUND = () -> new UserNotFoundException("The user with this id was not found");

    @Transactional
    @Override
    public void giveAdminRootToUser(long id) throws UserNotFoundException, IllegalUserException {
        User user = userRepository.findByIdAndIsActiveIsTrue(id).orElseThrow(USER_NOT_FOUND);
        if (user.getRole().equals(Role.ADMIN)) {
            throw new IllegalUserException("This user is already an administrator", HttpStatus.BAD_REQUEST, userMapper.toDto(user));
        }
        user.setRole(Role.ADMIN);
    }

    @Transactional
    @Override
    public User save(User user) throws UserAlreadyExistException {
        try {
            userRepository.save(user);
        } catch (RuntimeException ex) {
            throw new UserAlreadyExistException();
        }
        return user;
    }

    @Override
    public UserDto findById(long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findByIdAndIsActiveIsTrueAndRoleIs(id, Role.USER);
        return userMapper.toDto(user.orElseThrow(USER_NOT_FOUND));
    }

    @Override
    public UserDto findByUsername(String username) throws UserNotFoundException {
        Optional<User> user = userRepository.findByUsernameAndIsActiveIsTrue(username);
        return userMapper.toDto(user.orElseThrow(USER_NOT_FOUND));
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAllByIsActiveIsTrueAndRoleIs(Role.USER)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findUserOfAnyTypes(long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findByIdAndIsActiveIsTrue(id);
        return userMapper.toDto(user.orElseThrow(USER_NOT_FOUND));
    }

    @Override
    public List<UserDto> findAllUsersOfAnyTypes() {
        return userRepository.findAllByIsActiveIsTrue()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto deleteById(long id) throws UserNotFoundException, IllegalUserException {
        User user = userRepository.findById(id).orElseThrow(USER_NOT_FOUND);
        if (!user.isActive()) {
            throw new IllegalUserException("This user has already been deleted", HttpStatus.BAD_REQUEST, userMapper.toDto(user));
        }
        user.setActive(false);
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public void blockUser(long id) {
        User user = userRepository.findByIdAndIsActiveIsTrue(id).orElseThrow(USER_NOT_FOUND);
        if(!user.isNonLocked()) {
            throw new IllegalUserException("The user is already blocked", HttpStatus.FORBIDDEN, userMapper.toDto(user));
        }
        user.setNonLocked(false);
    }

    @Transactional
    @Override
    public void unblockUser(long id) {
        User user = userRepository.findByIdAndIsActiveIsTrue(id).orElseThrow(USER_NOT_FOUND);
        if(user.isNonLocked()) {
            throw new IllegalUserException("The user is already unblocked", HttpStatus.FORBIDDEN, userMapper.toDto(user));
        }
        loginAttemptService.deleteUserAttempts(user);
        user.setNonLocked(true);
    }
}
