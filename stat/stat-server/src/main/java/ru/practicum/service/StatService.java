package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatDto;
import ru.practicum.repository.StatRepository;

@Service
@AllArgsConstructor
public class StatService {
    @Autowired
    private StatRepository statRepository;

    public void create(StatDto statDto) {

    }
}
