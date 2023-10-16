package ru.yandex.praktikum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.model.dto.NewUserRequest;
import ru.yandex.praktikum.model.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(NewUserRequest userRequest);

    UserDto toUserDto(User user);


}
