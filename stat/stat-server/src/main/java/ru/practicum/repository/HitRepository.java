package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "SELECT new dto.practicum.ru.practicum.ViewStatsDto(eh.app, eh.uri, count(eh.uri)) FROM EndpointHit as eh WHERE eh.timestamp >= ?1 AND eh.timestamp <= ?2 GROUP BY eh.app, eh.uri")
    List<ViewStatsDto> getListEndpointHitsWOUri(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new dto.practicum.ru.practicum.ViewStatsDto(eh.app, eh.uri, count(eh.uri) as c) FROM EndpointHit as eh WHERE eh.uri in ?1 AND eh.timestamp >= ?2 AND eh.timestamp <= ?3 GROUP BY eh.app, eh.uri ORDER BY c desc")
    List<ViewStatsDto> getListEndpointHits(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new dto.practicum.ru.practicum.ViewStatsDto(eh.app, eh.uri, count(DISTINCT eh.ip)) FROM EndpointHit as eh WHERE eh.uri IN ?1 AND eh.timestamp >= ?2 AND eh.timestamp <= ?3 GROUP BY eh.app, eh.uri")
    List<ViewStatsDto> getListEndpointHitsDistinctIp(List<String> uris, LocalDateTime start, LocalDateTime end);
}
