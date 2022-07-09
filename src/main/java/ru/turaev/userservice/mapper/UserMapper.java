package ru.turaev.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.turaev.userservice.dto.LoginViewModel;
import ru.turaev.userservice.dto.UserDto;
import ru.turaev.userservice.entity.User;

@Mapper(componentModel = "spring", imports = ru.turaev.userservice.enums.Role.class)
public interface UserMapper {
    User fromDto(UserDto userDTO);

    UserDto toDto(User user);

    @Mapping(target = "role", expression = "java(Role.USER)")
    @Mapping(target = "active", expression = "java(true)")
    @Mapping(target = "nonLocked", expression = "java(true)")
    User fromLoginViewModel(LoginViewModel model);
}