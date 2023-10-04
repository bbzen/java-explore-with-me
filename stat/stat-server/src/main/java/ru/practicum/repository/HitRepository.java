package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "SELECT DISTINCT app FROM hits WHERE uri = ?1 AND timestamp >= ?2 AND timestamp <= ?3")
    List<String> getListApps(String uri, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT count(uri) AS uri_count FROM hits WHERE app = ?1 AND uri = ?2 AND timestamp >= ?3 AND timestamp <= ?4")
    Integer getUriCountByApp(String app, String uri, LocalDateTime start, LocalDateTime end);
}
