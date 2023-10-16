package ru.yandex.praktikum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.praktikum.model.Request;
import ru.yandex.praktikum.model.dto.ParticipationRequestDto;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    ParticipationRequestDto toParticipationRequestDto(Request request);
}
