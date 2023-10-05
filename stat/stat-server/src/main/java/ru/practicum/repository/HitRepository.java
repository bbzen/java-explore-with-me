package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "SELECT new ru.practicum.dto.ViewStatsDto(eh.app, eh.uri, CAST(count(eh.uri) AS int)) FROM EndpointHit as eh WHERE eh.uri in ?1 AND eh.timestamp >= ?2 AND eh.timestamp <= ?3 GROUP BY eh.app, eh.uri")
    List<ViewStatsDto> getListEndpointHits(String[] uris, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.dto.ViewStatsDto(eh.app, eh.uri, CAST(count(eh.uri) AS int)) FROM EndpointHit as eh WHERE eh.uri in ?1 AND eh.timestamp >= ?2 AND eh.timestamp <= ?3 AND eh.ip IN (SELECT DISTINCT ip FROM EndpointHit) GROUP BY eh.app, eh.uri")
    List<ViewStatsDto> getListEndpointHitsDistinctIp(String[] uris, LocalDateTime start, LocalDateTime end);
}
