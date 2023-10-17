package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.model.User;
import ru.practicum.model.dto.NewUserRequest;
import ru.practicum.model.dto.UserDto;
import ru.practicum.model.dto.UserShortDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(NewUserRequest userRequest);

    UserDto toUserDto(User user);

    UserShortDto toUserShortDto(User user);
}
