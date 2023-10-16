package ru.yandex.praktikum.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.praktikum.exception.DataAccessException;
import ru.yandex.praktikum.exception.NotFoundException;
import ru.yandex.praktikum.exception.OnConflictException;
import ru.yandex.praktikum.mapper.EventMapper;
import ru.yandex.praktikum.mapper.LocationMapper;
import ru.yandex.praktikum.mapper.RequestMapper;
import ru.yandex.praktikum.model.*;
import ru.yandex.praktikum.model.dto.*;
import ru.yandex.praktikum.model.status.EventStatus;
import ru.yandex.praktikum.model.status.ParticipationRequestStatus;
import ru.yandex.praktikum.model.status.StateAction;
import ru.yandex.praktikum.repository.*;

import java.time.LocalDateTime;
import java.util.*;
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
    private final RequestRepository requestRepository;
    @Autowired
    private final EventMapper eventMapper;
    @Autowired
    private final LocationMapper locationMapper;
    @Autowired
    private final RequestMapper requestMapper;

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
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest dto) {
        checkNewEventDto(dto);
        Event currentEvent = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found."));
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("There is no user with such id: " + userId ));
        if (currentEvent.getInitiator().getId() != currentUser.getId()) {
            throw new DataAccessException("The user isn`t an initiator of the Event. Restricted to update.");
        }
        if (dto.getLocation() != null) {
            currentEvent.setLocation(getLocation(dto.getLocation()));
        }
        if (currentEvent.getState() == EventStatus.PUBLISHED) {
            throw new OnConflictException("It`s allowed to change Events in pending or canceled status.");
        }
        if (dto.getCategory() != null) {
            currentEvent.setCategory(categoryRepository.findById(dto.getCategory()).orElseThrow(
                    () -> new NotFoundException("Category " + dto.getCategory() + " was not found.")));
        }
        Optional.ofNullable(dto.getAnnotation()).ifPresent(currentEvent::setAnnotation);
        Optional.ofNullable(dto.getDescription()).ifPresent(currentEvent::setDescription);
        Optional.ofNullable(dto.getEventDate()).ifPresent(currentEvent::setEventDate);
        Optional.ofNullable(dto.getPaid()).ifPresent(currentEvent::setPaid);
        Optional.ofNullable(dto.getParticipantLimit()).ifPresent(currentEvent::setParticipantLimit);
        Optional.ofNullable(dto.getRequestModeration()).ifPresent(currentEvent::setRequestModeration);
        Optional.ofNullable(dto.getTitle()).ifPresent(currentEvent::setTitle);
        if (dto.getStateAction() == StateAction.SEND_TO_REVIEW) {
            currentEvent.setState(EventStatus.PENDING);
        } else if (dto.getStateAction() == StateAction.CANCEL_REVIEW) {
            currentEvent.setState(EventStatus.CANCELED);
        }
        return eventMapper.toEventFullDto(currentEvent);
    }

    //GET /users/{userId}/events/{eventId}/requests
    //Получение информации о запросах на участие в событии текущего пользователя
    public List<ParticipationRequestDto> findAllParticipationRequests(Long userId, Long eventId) {
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found."));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("There is no user with such id: " + userId ));
        return requestRepository.findAllByEventId(eventId).stream().map(requestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    //PATCH /users/{userId}/events/{eventId}/requests
    //Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
    public EventRequestStatusUpdateResult updateParticipationRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest dto) {
        Event currentEvent = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found."));
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("There is no user with such id: " + userId ));
        if (currentEvent.getInitiator().getId() != currentUser.getId()) {
            throw new DataAccessException("The user isn`t an initiator of the Event. Restricted to update.");
        }

        if (!currentEvent.getRequestModeration() || currentEvent.getParticipantLimit() == 0) {
            throw new OnConflictException("There is no need in moderation.");
        }

        Long confirmedRequestsNumber = requestRepository.countByEventIdAndStatus(eventId, ParticipationRequestStatus.APPROVED);

        //compare participants number in repo with its limit
        if (currentEvent.getParticipantLimit() != 0 && currentEvent.getParticipantLimit() <= confirmedRequestsNumber) {
            throw new OnConflictException("The number of participants got the limit.");
        }

        //get list of requests by ids from dto and compare them with each other
        List<Request> requests = requestRepository.findAllByIdIn(dto.getRequestIds());
        Set<Long> actualIds = requests.stream().map(Request::getId).collect(Collectors.toSet());
        if (!actualIds.containsAll(new HashSet<>(dto.getRequestIds()))) {
            throw new NotFoundException("Ids list in request is invalid. Part of was not found");
        }

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests =  new ArrayList<>();

        for (Request request : requests) {
            if (!request.getStatus().equals(ParticipationRequestStatus.PENDING)) {
                throw new OnConflictException("Unable to update request not in pending state.");
            }
            if (!request.getEvent().getId().equals(eventId)) {
                rejectedRequests.add(requestMapper.toParticipationRequestDto(request));
                continue;
            }
            switch (dto.getStatus()) {
                case CONFIRMED:
                    if (confirmedRequestsNumber < currentEvent.getParticipantLimit()) {
                        request.setStatus(ParticipationRequestStatus.APPROVED);
                        confirmedRequestsNumber++;
                        confirmedRequests.add(requestMapper.toParticipationRequestDto(request));
                    } else {
                        request.setStatus(ParticipationRequestStatus.REJECTED);
                        rejectedRequests.add(requestMapper.toParticipationRequestDto(request));
                        throw new OnConflictException("Participants limit has been reached.");
                    }
                    break;
                case REJECTED:
                    request.setStatus(ParticipationRequestStatus.REJECTED);
                    rejectedRequests.add(requestMapper.toParticipationRequestDto(request));
                    break;
            }
        }

        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private void checkNewEventDto(DatedEvent dto) {
        if (LocalDateTime.now().plusHours(2).isAfter(dto.getEventDate())) {
            throw new OnConflictException("Event time can`t be in two hours before beginning.");
        }
    }

    //gets Location from repo if it exists, and saves if not.
    private Location getLocation(NewLocationDto dto) {
        return locationRepository.findByLatAndLon(dto.getLat(), dto.getLon()).orElse(locationRepository.save(locationMapper.toLocation(dto)));
    }
}
