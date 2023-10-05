package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.List;

public interface HitService {

    void create(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getHits(String start, String end, String[] uris, boolean unique);
}
