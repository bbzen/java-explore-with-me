package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

@Mapper(componentModel = "spring")
public interface HitMapper {
    HitMapper INSTANCE = Mappers.getMapper(HitMapper.class);

    EndpointHit toStat(EndpointHitDto endpointHitDto);
}
