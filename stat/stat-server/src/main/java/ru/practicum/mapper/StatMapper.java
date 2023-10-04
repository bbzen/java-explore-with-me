package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.StatInputDto;
import ru.practicum.model.Stat;

@Mapper(componentModel = "spring")
public interface StatMapper {
    static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
    StatMapper INSTANCE = Mappers.getMapper(StatMapper.class);

    @Mapping(source = "timestamp", target = "timestamp", dateFormat = dateTimePattern)
    Stat toStat(StatInputDto statInputDto);
}
