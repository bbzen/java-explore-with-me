package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.HitService;

import java.util.List;

@RestController
@AllArgsConstructor
public class StatServerController {
    @Autowired
    private HitService hitService;

    @PostMapping("/hit")
    public void createHit(@RequestBody EndpointHitDto dto) {
        hitService.create(dto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam String start, @RequestParam String end, @RequestParam(required = false) String[] uris, @RequestParam(required = false, defaultValue = "false") boolean unique) {
        return hitService.getHits(start, end, uris, unique);
    }
}
