package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatClient;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.OnConflictException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;
import ru.practicum.model.dto.*;
import ru.practicum.model.status.EventState;
import ru.practicum.model.status.RequestStatus;
import ru.practicum.model.status.StateActionAdm;
import ru.practicum.repository.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EwmServiceServerAdmin {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final LocationRepository locationRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private final UserMapper userMapper;
    @Autowired
    private final CategoryMapper categoryMapper;
    @Autowired
    private final LocationMapper locationMapper;
    @Autowired
    private final EventMapper eventMapper;
    @Autowired
    private final StatClient statClient;
    private final static String DATETIMEPATTERN = "yyyy-MM-dd HH:mm:ss";
    @Autowired
    private RequestRepository requestRepository;

    //GET /admin/users
    //Получение информации о пользователях
    public List<UserDto> findUsers(List<Long> ids, Integer from, Integer size) {
        List<User> result;
        if (ids != null && !ids.isEmpty()) {
            result = userRepository.findAllById(ids);
        } else {
            Pageable page = PageRequest.of(Math.abs(from / size), size);
            result = userRepository.findAll(page).toList();
        }
        return result.stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    //POST /admin/users
    //Добавление нового пользователя
    public UserDto createUser(NewUserRequest userRequest) {
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userRequest)));
    }

    //DELETE /admin/users/{userId}
    //Удаление пользователя
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User " + userId + " not found.");
        }
        userRepository.deleteById(userId);
    }


    //POST /admin/categories
    //Добавление новой категории
    public CategoryDto createCategory(NewCategoryDto dto) {
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(dto)));
    }

    //PATCH /admin/categories/{catId}
    //Изменение категории
    public CategoryDto updateCategory(NewCategoryDto dto, Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Category " + catId + " not found.");
        }
        Category catToSave = categoryMapper.toCategory(dto);
        catToSave.setId(catId);
        return categoryMapper.toCategoryDto(categoryRepository.save(catToSave));
    }

    //DELETE /admin/categories/{catId}
    //Удаление категории
    public void deleteCategory(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Category " + catId + " not found.");
        }
        categoryRepository.deleteById(catId);
    }

    //GET /admin/events
    //Поиск событий
    public List<EventFullDto> findAllEvents(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        Pageable page = PageRequest.of(Math.abs(from / size), size);
        rangeStart = rangeStart == null ? LocalDateTime.now() : rangeStart;
        rangeEnd = rangeEnd == null ? LocalDateTime.MAX : rangeEnd;

        List<Event> events = eventRepository.findAllByAdmin(users, states, categories, rangeStart, rangeEnd, page);

        List<String> eventUrls = events.stream()
                .map(e -> "/events/" + e.getId())
                .collect(Collectors.toList());

        Map<String, Object> paramsForRequest = Map.of(
                "start", rangeStart.format(DateTimeFormatter.ofPattern(DATETIMEPATTERN)),
                "end", rangeEnd.format(DateTimeFormatter.ofPattern(DATETIMEPATTERN)),
                "uris", eventUrls,
                "unique", true
        );
        List<ViewStatsDto> viewStatsDtos = statClient.getStats(paramsForRequest);

        return events.stream()
                .map(eventMapper::toEventFullDto)
                .peek(eventFullDto -> {
                    Optional<ViewStatsDto> viewStatsDto = viewStatsDtos.stream()
                            .filter(statsDto -> statsDto.getUri().equals("/events/" + eventFullDto.getId()))
                            .findFirst();
                    eventFullDto.setViews(viewStatsDto.map(ViewStatsDto::getHits).orElse(0L));
                }).peek(eventFullDto -> eventFullDto.setConfirmedRequests(
                        requestRepository.countByEventIdAndStatus(eventFullDto.getId(), RequestStatus.APPROVED)))
                .collect(Collectors.toList());
    }

    //PATCH /admin/events/{eventId}
    //Редактирование данных любого события администратором.
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest adminRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event " + eventId + " was not found."));
        checkDateTimeUEAR(adminRequest);
        checkRequestState(adminRequest, event);

        if (adminRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(adminRequest.getCategory()).orElseThrow(
                    () -> new NotFoundException("There is no category with id " + adminRequest.getCategory())));
        }

        if (adminRequest.getLocation() != null) {
            event.setLocation(getLocation(adminRequest.getLocation()));
        }
        Optional.ofNullable(adminRequest.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(adminRequest.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(adminRequest.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(adminRequest.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(adminRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(adminRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(adminRequest.getRequestModeration()).ifPresent(event::setRequestModeration);

        switch (adminRequest.getStateActionAdm()) {
            case PUBLISH_EVENT:
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                break;
            case REJECT_EVENT:
                event.setState(EventState.CANCELED);
                break;

        }
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    private Location getLocation(NewLocationDto dto) {
        return locationRepository.findByLatAndLon(dto.getLat(), dto.getLon())
                .orElse(locationRepository.save(locationMapper.toLocation(dto)));
    }

    private void checkDateTimeUEAR(UpdateEventAdminRequest updateEventAdminRequest) {
        if (updateEventAdminRequest.getEventDate() == null || LocalDateTime.now().plusHours(1).isAfter(updateEventAdminRequest.getEventDate())) {
            throw new OnConflictException("The event time and date cant be set in an hour before event.");
        }
    }

    private void checkRequestState(UpdateEventAdminRequest adminRequest, Event event) {
        if (adminRequest.getStateActionAdm() != null) {
            if (adminRequest.getStateActionAdm().equals(StateActionAdm.PUBLISH_EVENT) && !event.getState().equals(EventState.PENDING)) {
                throw new OnConflictException("Bad state of admin action. Restricted to publish");
            } else if (adminRequest.getStateActionAdm().equals(StateActionAdm.REJECT_EVENT) && event.getState().equals(EventState.PUBLISHED)) {
                throw new OnConflictException("Bad state of admin action. Restricted to reject");
            }
        }
    }
}
