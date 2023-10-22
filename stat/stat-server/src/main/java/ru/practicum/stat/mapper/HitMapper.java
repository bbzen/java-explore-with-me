package ru.practicum.stat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.model.EndpointHit;

@Mapper(componentModel = "spring")
public interface HitMapper {
    HitMapper INSTANCE = Mappers.getMapper(HitMapper.class);

    EndpointHit toStat(EndpointHitDto endpointHitDto);
}
