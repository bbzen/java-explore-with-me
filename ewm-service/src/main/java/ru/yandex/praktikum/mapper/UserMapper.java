package ru.yandex.praktikum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.praktikum.model.NewUserRequest;
import ru.yandex.praktikum.model.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(NewUserRequest userRequest);
}
