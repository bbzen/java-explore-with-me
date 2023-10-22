package ru.practicum.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stat.dto.ViewStatsDto;
import ru.practicum.stat.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "SELECT new ru.practicum.stat.dto.ViewStatsDto(eh.app, eh.uri, count(eh.uri) as c) FROM EndpointHit as eh WHERE eh.timestamp >= ?1 AND eh.timestamp <= ?2 GROUP BY eh.app, eh.uri ORDER BY c desc")
    List<ViewStatsDto> getListEndpointHitsWOUri(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.stat.dto.ViewStatsDto(eh.app, eh.uri, count(eh.uri) as c) FROM EndpointHit as eh WHERE eh.uri in ?1 AND eh.timestamp >= ?2 AND eh.timestamp <= ?3 GROUP BY eh.app, eh.uri ORDER BY c desc")
    List<ViewStatsDto> getListEndpointHits(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.stat.dto.ViewStatsDto(eh.app, eh.uri, count(DISTINCT eh.ip) as hits) FROM EndpointHit as eh WHERE eh.uri IN ?1 AND eh.timestamp between ?2 and ?3 GROUP BY eh.app, eh.uri ORDER BY hits desc")
    List<ViewStatsDto> getListEndpointHitsDistinctIp(List<String> uris, LocalDateTime start, LocalDateTime end);
}
