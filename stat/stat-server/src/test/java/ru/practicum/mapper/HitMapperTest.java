package ru.practicum.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class HitMapperTest {
@Autowired
private HitMapper hitMapper;
    @Test
    public void toHitNormal() {
        EndpointHitDto dto = new EndpointHitDto("ewm-main-service", "/events/1", "192.163.0.1", "2022-09-06 11:00:23");
        EndpointHit result = hitMapper.toStat(dto);
        LocalDateTime resultDateTime = LocalDateTime.parse(dto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        assertEquals(dto.getApp(), result.getApp());
        assertEquals(dto.getUri(), result.getUri());
        assertEquals(dto.getIp(), result.getIp());
        assertEquals(resultDateTime, result.getTimestamp());
    }
}