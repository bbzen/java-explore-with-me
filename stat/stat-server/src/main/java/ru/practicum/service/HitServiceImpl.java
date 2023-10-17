package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exception.HitInputDataInvalidException;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class HitServiceImpl implements HitService {
    @Autowired
    private HitRepository hitRepository;
    @Autowired
    private HitMapper mapper;

    @Override
    public void create(EndpointHitDto endpointHitDto) {
        checkEndpointHitDto(endpointHitDto);
        EndpointHit endpointHit = hitRepository.save(mapper.toStat(endpointHitDto));
    }

    @Override
    public List<ViewStatsDto> getHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start == null || end == null || start.isAfter(end)) {
            throw new HitInputDataInvalidException("Start or End didn`t set, or start is before end.");
        }
        List<ViewStatsDto> resultList;
        if (uris == null) {
            resultList = hitRepository.getListEndpointHitsWOUri(start, end);
        } else if (unique) {
            resultList = hitRepository.getListEndpointHitsDistinctIp(uris, start, end);
        } else {
            resultList = hitRepository.getListEndpointHits(uris, start, end);
        }
        return resultList;
    }

    private void checkEndpointHitDto(EndpointHitDto dto) {
        if (dto.getApp().isBlank() || dto.getUri().isBlank() || dto.getIp().isBlank() || dto.getTimestamp() == null) {
            throw new HitInputDataInvalidException("Check all fields. Fields have to be filled.");
        }
    }
}
