package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.dto.NewUserRequest;
import ru.practicum.ewm.model.dto.UserDto;
import ru.practicum.ewm.model.dto.UserShortDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(NewUserRequest userRequest);

    UserDto toUserDto(User user);

    UserShortDto toUserShortDto(User user);
}
