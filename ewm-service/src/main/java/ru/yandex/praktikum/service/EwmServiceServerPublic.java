package ru.yandex.praktikum.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.client.StatClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.yandex.praktikum.exception.BadRequestException;
import ru.yandex.praktikum.exception.NotFoundException;
import ru.yandex.praktikum.mapper.CategoryMapper;
import ru.yandex.praktikum.mapper.EventMapper;
import ru.yandex.praktikum.model.Event;
import ru.yandex.praktikum.model.dto.CategoryDto;
import ru.yandex.praktikum.model.dto.EventFullDto;
import ru.yandex.praktikum.model.dto.EventShortDto;
import ru.yandex.praktikum.model.status.EventStatus;
import ru.yandex.praktikum.model.status.ParticipationRequestStatus;
import ru.yandex.praktikum.repository.CategoryRepository;
import ru.yandex.praktikum.repository.EventRepository;
import ru.yandex.praktikum.repository.RequestRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
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
    private final CategoryMapper categoryMapper;
    @Autowired
    private final EventMapper eventMapper;
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
        return categoryMapper.toCategoryDto(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("There is no such category with id " + catId)));
    }

    //GET /events
    //Получение событий с возможностью фильтрации
    public List<EventShortDto> findEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Start must to be before end date");
        }

        statClient.postHit(EndpointHitDto.builder()
                .app("ewm")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());



        return null;
    }

    //GET /events/{id}
    //Получение подробной информации об опубликованном событии по его идентификатору
    public EventFullDto findEventById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event " + eventId + " not found."));

        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            throw new NotFoundException("Event " + eventId + " was not published.");
        }
        statClient.postHit(EndpointHitDto.builder()
                .app("ewm")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());

        Map<String, Object> paramsForRequest = Map.of(
                "start", LocalDateTime.MIN.format(DateTimeFormatter.ofPattern(DATETIMEPATTERN)),
                "end", LocalDateTime.MAX.format(DateTimeFormatter.ofPattern(DATETIMEPATTERN)),
                "uris", List.of("/events/" + event.getId()),
                "unique", true
        );
        List<ViewStatsDto> viewStatsDtos = statClient.getStats(paramsForRequest);
        Long numberOfConfirmedRequests = requestRepository.countByEventIdAndStatus(eventId, ParticipationRequestStatus.APPROVED);
        Long numberOfViews = viewStatsDtos.isEmpty() ? 0L : viewStatsDtos.get(0).getHits();

        EventFullDto result = eventMapper.toEventFullDto(event);
        result.setConfirmedRequests(numberOfConfirmedRequests);
        result.setViews(numberOfViews);
        return result;
    }
}


