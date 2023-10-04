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
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class HitService {
    @Autowired
    private HitRepository hitRepository;
    @Autowired
    private HitMapper mapper;

    public void create(EndpointHitDto endpointHitDto) {
        checkEndpointHitDto(endpointHitDto);
        hitRepository.save(mapper.toStat(endpointHitDto));
    }

    public List<ViewStatsDto> getHits(String start, String end, String[] uris, boolean unique) {
        checkGetParams(start, end, uris);
        String startDecoded = URLDecoder.decode(start, StandardCharsets.UTF_8);
        String endDecoded = URLDecoder.decode(end, StandardCharsets.UTF_8);
        LocalDateTime startLDT = LocalDateTime.parse(startDecoded, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endLDT = LocalDateTime.parse(endDecoded, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        List<ViewStatsDto> resultList = new ArrayList<>();
        if (unique) {

        } else {
            for (String uri : uris) {
                List<String> resultApps = hitRepository.getListApps(uri, startLDT, endLDT);
                for (String resultApp : resultApps) {
                    Integer hits = hitRepository.getUriCountByApp(resultApp, uri, startLDT, endLDT);
                    resultList.add(new ViewStatsDto(resultApp, uri, hits));
                }
            }
        }
        return resultList;
    }

    private void checkEndpointHitDto(EndpointHitDto dto) {
        if (dto.getApp().isBlank() || dto.getUri().isBlank() || dto.getIp().isBlank() || dto.getTimestamp() == null) {
            throw new HitInputDataInvalidException("Проверьте аргументы для добавления статистики. Должны быть заполнены все поля.");
        }
    }

    private void checkGetParams(String start, String end, String[] uris) {
        if (start.isBlank() || end.isBlank() || uris.length == 0) {
            throw new HitInputDataInvalidException("Проверьте аргументы для получения статистики. Должны быть заполнены все поля.");
        }
    }
}
