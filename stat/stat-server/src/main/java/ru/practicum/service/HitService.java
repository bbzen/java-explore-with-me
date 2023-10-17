package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {

    void create(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
