package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<EndpointHit, Long> {

    //SELECT eh.app, eh.uri, count(eh.uri) FROM hits as eh WHERE eh.timestamp >= '2020-05-05 00:00:00' AND eh.timestamp <= '2035-05-05 00:00:00' GROUP BY eh.app, eh.uri;
    @Query(value = "SELECT new ru.practicum.dto.ViewStatsDto(eh.app, eh.uri, CAST(count(eh.uri) AS int)) FROM EndpointHit as eh WHERE eh.timestamp >= ?1 AND eh.timestamp <= ?2 GROUP BY eh.app, eh.uri")
    List<ViewStatsDto> getListEndpointHitsWOUri(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.dto.ViewStatsDto(eh.app, eh.uri, CAST(count(eh.uri) AS int) as c) FROM EndpointHit as eh WHERE eh.uri in ?1 AND eh.timestamp >= ?2 AND eh.timestamp <= ?3 GROUP BY eh.app, eh.uri ORDER BY c desc")
    List<ViewStatsDto> getListEndpointHits(String[] uris, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.dto.ViewStatsDto(eh.app, eh.uri, CAST(count(DISTINCT eh.ip) AS int)) FROM EndpointHit as eh WHERE eh.uri IN ?1 AND eh.timestamp >= ?2 AND eh.timestamp <= ?3 GROUP BY eh.app, eh.uri")
    List<ViewStatsDto> getListEndpointHitsDistinctIp(String[] uris, LocalDateTime start, LocalDateTime end);
}
