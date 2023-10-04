package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.service.HitService;

@RestController
@AllArgsConstructor
public class StatServerController {
    @Autowired
    private HitService hitService;

    @PostMapping("/hit")
    public void create(@RequestBody EndpointHitDto dto) {
        hitService.create(dto);
    }
}
