package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.StatInputDto;
import ru.practicum.model.Stat;

@Mapper(componentModel = "spring")
public interface StatMapper {
    Stat toStat(StatInputDto statInputDto);
}
