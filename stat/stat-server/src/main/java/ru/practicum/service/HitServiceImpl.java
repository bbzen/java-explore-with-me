package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exception.HitInputDataInvalidException;
import ru.practicum.mapper.HitMapper;
import ru.practicum.repository.HitRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class HitServiceImpl implements HitService {
    @Autowired
    private HitRepository hitRepository;
    @Autowired
    private HitMapper mapper;

    public void create(EndpointHitDto endpointHitDto) {
        checkEndpointHitDto(endpointHitDto);
        hitRepository.save(mapper.toStat(endpointHitDto));
    }

    public List<ViewStatsDto> getHits(String start, String end, String[] uris, boolean unique) {
        checkGetParams(start, end);
        String startDecoded = URLDecoder.decode(start, StandardCharsets.UTF_8);
        String endDecoded = URLDecoder.decode(end, StandardCharsets.UTF_8);
        LocalDateTime startLDT = LocalDateTime.parse(startDecoded, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endLDT = LocalDateTime.parse(endDecoded, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        List<ViewStatsDto> resultList;
        if (uris == null) {
            resultList = hitRepository.getListEndpointHitsWOUri(startLDT, endLDT);
        } else if (unique) {
            resultList = hitRepository.getListEndpointHitsDistinctIp(uris, startLDT, endLDT);
        } else {
            resultList = hitRepository.getListEndpointHits(uris, startLDT, endLDT);
        }
        return resultList;
    }

    private void checkEndpointHitDto(EndpointHitDto dto) {
        if (dto.getApp().isBlank() || dto.getUri().isBlank() || dto.getIp().isBlank() || dto.getTimestamp() == null) {
            throw new HitInputDataInvalidException("Проверьте аргументы для добавления статистики. Должны быть заполнены все поля.");
        }
    }

    private void checkGetParams(String start, String end) {
        if (start.isBlank() || end.isBlank()) {
            throw new HitInputDataInvalidException("Проверьте аргументы для получения статистики. Должен быть задан период получения статистики");
        }
    }
}
