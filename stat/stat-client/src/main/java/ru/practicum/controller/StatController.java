package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatClnt;
import ru.practicum.dto.EndpointHitDto;

import java.util.Map;

@RestController
@AllArgsConstructor
public class StatController {
    @Autowired
    private StatClnt statClnt;

    @PostMapping("/hit")
    public void createHit(@RequestBody EndpointHitDto dto) {
        statClnt.postHit(dto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam String start, @RequestParam String end, @RequestParam(required = false) String[] uris, @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        Map<String, Object> params = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        return statClnt.getStats(params);
    }
}
