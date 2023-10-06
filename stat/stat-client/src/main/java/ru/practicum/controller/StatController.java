package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatClnt;
import ru.practicum.dto.EndpointHitDto;

import java.util.HashMap;
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
    public ResponseEntity<Object> getStats(@RequestParam String start, @RequestParam String end, @RequestParam(required = false) String[] uris, @RequestParam(defaultValue = "false") Boolean unique) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        if (uris != null) {
            params.put("uris", uris);
        }
        params.put("unique", unique);
        return statClnt.getStats(params);
    }
}
