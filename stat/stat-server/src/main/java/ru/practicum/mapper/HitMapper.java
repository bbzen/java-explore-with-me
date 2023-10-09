package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

@Mapper(componentModel = "spring")
public interface HitMapper {
    static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
    HitMapper INSTANCE = Mappers.getMapper(HitMapper.class);

    @Mapping(source = "timestamp", target = "timestamp", dateFormat = dateTimePattern)
    EndpointHit toStat(EndpointHitDto endpointHitDto);
}
