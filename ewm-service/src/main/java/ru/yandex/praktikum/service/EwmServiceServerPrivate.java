package ru.yandex.praktikum.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.praktikum.exception.DataAccessException;
import ru.yandex.praktikum.exception.EntityValidationException;
import ru.yandex.praktikum.exception.NotFoundException;
import ru.yandex.praktikum.mapper.EventMapper;
import ru.yandex.praktikum.model.Event;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.model.dto.DatedEvent;
import ru.yandex.praktikum.model.dto.EventFullDto;
import ru.yandex.praktikum.model.dto.NewEventDto;
import ru.yandex.praktikum.model.dto.UpdateEventUserRequest;
import ru.yandex.praktikum.model.status.EventStatus;
import ru.yandex.praktikum.model.status.StateAction;
import ru.yandex.praktikum.repository.EventRepository;
import ru.yandex.praktikum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EwmServiceServerPrivate {
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private EventMapper eventMapper;

    //GET /users/{userId}/events
    // Получение событий, добавленных текущим пользователем
    public List<EventFullDto> findAllUserEvents(Long userId) {
        return eventRepository.findAllByInitiator(userId).stream().map(eventMapper::toEventFullDto).collect(Collectors.toList());
    }

    //POST /users/{userId}/events
    // Добавление нового события
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        checkNewEventDto(newEventDto);
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("There is no user with such id: " + userId ));
        Event result = eventMapper.toEventFromNew(newEventDto);
        result.setCreatedOn(LocalDateTime.now());
        result.setInitiator(initiator);
        result.setState(EventStatus.PENDING);
        result.setViews(0L);
        return eventMapper.toEventFullDto(eventRepository.save(result));
    }

    // GET /users/{userId}/events/{eventId}
    // Получение полной информации о событии добавленном текущим пользователем
    public EventFullDto findUserEvent(Long userId, Long eventId) {
        Event result = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event" + eventId + "not found."));
        if (result.getInitiator().getId() != userId) {
            throw new DataAccessException("The user isn`t an initiator of the Event. Restricted to edit.");
        }
        return eventMapper.toEventFullDto(result);
    }

    //PATCH /users/{userId}/events/{eventId}
    //Изменение события добавленного текущим пользователем
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updEvent) {
        checkNewEventDto(updEvent);
        Event srcEvent = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found."));
        if (srcEvent.getState() == EventStatus.PUBLISHED) {
            throw new EntityValidationException("It`s not able to change Event not in Pending or Cancel state.");
        }
        if (srcEvent.getInitiator().getId() != userId) {
            throw new DataAccessException("The user isn`t an initiator of the Event. Restricted to edit.");
        }
        Event result = eventMapper.toEventFromUpdUser(updEvent);
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("There is no user with such id: " + userId ));
        result.setId(eventId);
        result.setConfirmedRequests(srcEvent.getConfirmedRequests());
        result.setCreatedOn(srcEvent.getCreatedOn());
        result.setInitiator(initiator);
        result.setState(srcEvent.getState());
        result.setViews(srcEvent.getViews());
        StateAction currentAction = updEvent.getStateAction();
        //todo something with statAction
        return eventMapper.toEventFullDto(eventRepository.save(result));
    }

    private void checkNewEventDto(DatedEvent dto) {
        if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EntityValidationException("Event time can`t be in 2 hours before beginning.");
        }
    }
}
