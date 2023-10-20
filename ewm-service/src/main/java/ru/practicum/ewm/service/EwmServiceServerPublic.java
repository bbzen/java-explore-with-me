package ru.practicum.ewm.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.dto.CategoryDto;
import ru.practicum.ewm.model.dto.CompilationDto;
import ru.practicum.ewm.model.dto.EventFullDto;
import ru.practicum.ewm.model.dto.EventShortDto;
import ru.practicum.ewm.model.status.EventState;
import ru.practicum.ewm.model.status.RequestStatus;
import ru.practicum.ewm.model.status.SortStatus;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.stat.client.StatClient;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EwmServiceServerPublic {
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    private final RequestRepository requestRepository;
    @Autowired
    private final CompilationRepository compilationRepository;
    @Autowired
    private final CategoryMapper categoryMapper;
    @Autowired
    private final EventMapper eventMapper;
    @Autowired
    private final CompilationMapper compilationMapper;
    @Autowired
    private final StatClient statClient;
    private final static String DATETIMEPATTERN = "yyyy-MM-dd HH:mm:ss";

    //GET /categories
    //Получение категорий
    public List<CategoryDto> findAllCategories(Integer from, Integer size) {
        if (from < 0 || size < 0) {
            throw new BadRequestException("From and size params cant be negative.");
        }
        Pageable page = PageRequest.of(Math.abs(from / size), size);
        return categoryRepository.findAll(page).toList().stream().map(categoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    //GET /categories/{catId}
    //Получение информации о категории по её идентификатору
    public CategoryDto findCategory(@PathVariable Long catId) {
        return categoryMapper.toCategoryDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("There is no such category with id " + catId)));
    }

    //GET /events
    //Получение событий с возможностью фильтрации
    public List<EventShortDto> findEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            SortStatus sort,
            Integer from,
            Integer size,
            HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Start must to be before end date");
        }

        statClient.postHit(EndpointHitDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());

        Pageable page = PageRequest.of(Math.abs(from / size), size);

        if (categories != null && categories.size() == 1 && categories.get(0) == 0L) {
            categories = null;
        }
        rangeStart = rangeStart == null ? LocalDateTime.now() : rangeStart;
        rangeEnd = rangeEnd == null ? LocalDateTime.now().plusYears(1) : rangeEnd;

        List<Event> events = eventRepository.findAllByPublic(text, categories, paid, rangeStart, rangeEnd, page);

        if (onlyAvailable) {
            events = getOnlyAvailableEvents(events);
        }

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


        List<EventShortDto> eventShortDtos = events.stream()
                .map(eventMapper::toEventShortDto)
                .peek(eventShortDto -> {
                    Optional<ViewStatsDto> viewStatsDto = viewStatsDtos.stream()
                            .filter(statsDto -> statsDto.getUri().equals("/events/" + eventShortDto.getId()))
                            .findFirst();
                    eventShortDto.setViews(viewStatsDto.map(ViewStatsDto::getHits).orElse(0L));
                }).peek(dto -> dto.setConfirmedRequests(
                        requestRepository.countByEventIdAndStatus(dto.getId(), RequestStatus.CONFIRMED)))
                .collect(Collectors.toList());

        switch (sort) {
            case EVENT_DATE:
                eventShortDtos.sort(Comparator.comparing(EventShortDto::getEventDate));
                break;
            case VIEWS:
                eventShortDtos.sort(Comparator.comparing(EventShortDto::getViews).reversed());
                break;
        }

        if (from >= eventShortDtos.size()) {
            return Collections.emptyList();
        }

        int toIndex = Math.min(from + size, eventShortDtos.size());
        return eventShortDtos.subList(from, toIndex);
    }

    //GET /events/{id}
    //Получение подробной информации об опубликованном событии по его идентификатору
    public EventFullDto findEventById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event " + eventId + " was not found."));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Event " + eventId + " was not published.");
        }
        statClient.postHit(EndpointHitDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());

        Long numberOfConfirmedRequests = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);

        Map<String, Object> paramsForRequest = Map.of(
                "start", LocalDateTime.now().minusYears(1).format(DateTimeFormatter.ofPattern(DATETIMEPATTERN)),
                "end", LocalDateTime.now().plusYears(1) .format(DateTimeFormatter.ofPattern(DATETIMEPATTERN)),
                "uris", List.of("/events/" + event.getId()),
                "unique", true
        );
        List<ViewStatsDto> viewStatsDtos = statClient.getStats(paramsForRequest);
        Long numberOfViews = viewStatsDtos.isEmpty() ? 0L : viewStatsDtos.get(0).getHits();

        EventFullDto result = eventMapper.toEventFullDto(event);
        result.setConfirmedRequests(numberOfConfirmedRequests);
        result.setViews(numberOfViews);
        return result;
    }

    //GET compilations
    //Получение подборки событий
    public List<CompilationDto> findAllCompilations(Boolean pinned, int from, int size) {
        Pageable page = PageRequest.of(Math.abs(from / size), size);
        return compilationRepository.findAllByPinnedIsNullOrPinned(pinned, page).stream()
                .map(compilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    //Get /compilations/{compId}
    //Получение подборки событий по его Id
    public CompilationDto findCompilationById(Long compId) {
        return compilationMapper.toCompilationDto(compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation " + compId + " was not found.")));
    }

    private List<Event> getOnlyAvailableEvents(List<Event> srcList) {
        return srcList.stream()
                .filter(e -> e.getParticipantLimit().equals(0) || e.getParticipantLimit() < requestRepository.countByEventIdAndStatus(e.getId(), RequestStatus.CONFIRMED))
                .collect(Collectors.toList());
    }
}


