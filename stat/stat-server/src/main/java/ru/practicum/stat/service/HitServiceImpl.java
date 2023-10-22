package ru.practicum.stat.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;
import ru.practicum.stat.exception.HitInputDataInvalidException;
import ru.practicum.stat.mapper.HitMapper;
import ru.practicum.stat.model.EndpointHit;
import ru.practicum.stat.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HitServiceImpl implements HitService {
    @Autowired
    private final HitRepository hitRepository;
    @Autowired
    private final HitMapper mapper;

    @Transactional
    @Override
    public void create(EndpointHitDto endpointHitDto) {
        checkEndpointHitDto(endpointHitDto);
        EndpointHit endpointHit = hitRepository.save(mapper.toStat(endpointHitDto));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ViewStatsDto> getHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start == null || end == null || start.isAfter(end)) {
            throw new HitInputDataInvalidException("Start or End didn`t set, or start is before end.");
        }

        List<String> listForRequest = uris;
        if (uris != null) {
            listForRequest = uris.stream().map(s -> s.replaceAll("[\\[\\]]", "")).collect(Collectors.toList());
        }

        List<ViewStatsDto> resultList;
        if (uris == null) {
            resultList = hitRepository.getListEndpointHitsWOUri(start, end);
        } else if (unique) {
            resultList = hitRepository.getListEndpointHitsDistinctIp(listForRequest, start, end);
        } else {
            resultList = hitRepository.getListEndpointHits(listForRequest, start, end);
        }
        return resultList;
    }

    private void checkEndpointHitDto(EndpointHitDto dto) {
        if (dto.getApp().isBlank() || dto.getUri().isBlank() || dto.getIp().isBlank() || dto.getTimestamp() == null) {
            throw new HitInputDataInvalidException("Check all fields. Fields have to be filled.");
        }
    }
}
