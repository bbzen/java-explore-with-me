package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.StatInputDto;
import ru.practicum.service.StatService;

@RestController
@AllArgsConstructor
public class StatServerController {
    @Autowired
    private StatService statService;

    @PostMapping("/hit")
    public void create(@RequestBody StatInputDto dto) {
        statService.create(dto);
    }
}
