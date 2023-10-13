package ru.yandex.praktikum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.praktikum.model.Event;
import ru.yandex.praktikum.model.dto.EventFullDto;
import ru.yandex.praktikum.model.dto.NewEventDto;
import ru.yandex.praktikum.model.dto.UpdateEventAdminRequest;
import ru.yandex.praktikum.model.dto.UpdateEventUserRequest;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    Event toEventFromNew(NewEventDto dto);

    Event toEventFromUpdUser(UpdateEventUserRequest dto);

    Event toEventFromUpdAdm(UpdateEventAdminRequest dto);

    EventFullDto toEventFullDto(Event event);
}
