package ru.yandex.praktikum.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.praktikum.exception.DataAccessException;
import ru.yandex.praktikum.exception.EntityValidationException;
import ru.yandex.praktikum.exception.NotFoundException;
import ru.yandex.praktikum.mapper.EventMapper;
import ru.yandex.praktikum.mapper.LocationMapper;
import ru.yandex.praktikum.model.Category;
import ru.yandex.praktikum.model.Event;
import ru.yandex.praktikum.model.Location;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.model.dto.*;
import ru.yandex.praktikum.model.status.EventStatus;
import ru.yandex.praktikum.model.status.StateAction;
import ru.yandex.praktikum.repository.CategoryRepository;
import ru.yandex.praktikum.repository.EventRepository;
import ru.yandex.praktikum.repository.LocationRepository;
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
    private final CategoryRepository categoryRepository;
    @Autowired
    private final LocationRepository locationRepository;
    @Autowired
    private final EventMapper eventMapper;
    @Autowired
    private final LocationMapper locationMapper;

    //GET /users/{userId}/events
    // Получение событий, добавленных текущим пользователем
    public List<EventFullDto> findAllUserEvents(Long userId) {
        return eventRepository.findAllByInitiator(userId).stream().map(eventMapper::toEventFullDto).collect(Collectors.toList());
    }

    //POST /users/{userId}/events
    // Добавление нового события
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        checkNewEventDto(newEventDto);
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("There is no user with such id: " + userId ));
        Event result = eventMapper.toEventFromNew(newEventDto);
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new NotFoundException("There is no category with id " + newEventDto.getCategory()));
        Location location = getLocation(newEventDto.getLocation());

        result.setCategory(category);
        result.setLocation(location);
        result.setCreatedOn(LocalDateTime.now());
        result.setInitiator(initiator);
        result.setState(EventStatus.PENDING);
        result.setViews(0L);
        result.setPaid(newEventDto.getPaid() != null && newEventDto.getPaid());
        result.setParticipantLimit(newEventDto.getParticipantLimit() == null ? 0 : newEventDto.getParticipantLimit());
        result.setRequestModeration(newEventDto.getRequestModeration() == null || newEventDto.getRequestModeration());
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

    //gets Location from repo if it exists, and saves if not.
    private Location getLocation(NewLocationDto dto) {
        return locationRepository.findByLatAndLon(dto.getLat(), dto.getLon()).orElse(locationRepository.save(locationMapper.toLocation(dto)));
    }
}
