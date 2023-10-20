package ru.practicum.stat.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;
import ru.practicum.stat.service.HitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
public class StatServerController {
    @Autowired
    private final HitService hitService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@RequestBody EndpointHitDto dto) {
        hitService.create(dto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(required = false, defaultValue = "false") boolean unique) {
        return hitService.getHits(start, end, uris, unique);
    }
}
