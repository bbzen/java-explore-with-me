package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatInputDto;
import ru.practicum.exception.StatInputDtoInvalidException;
import ru.practicum.mapper.StatMapper;
import ru.practicum.repository.StatRepository;

@Service
@AllArgsConstructor
public class StatService {
    @Autowired
    private StatRepository statRepository;
    @Autowired
    private StatMapper mapper;

    public void create(StatInputDto statInputDto) {
        checkStatInputDto(statInputDto);

        statRepository.save(mapper.toStat(statInputDto));
    }

    private void checkStatInputDto(StatInputDto dto) {
        if (dto.getApp().isBlank() || dto.getUri().isBlank() || dto.getIp().isBlank() || dto.getTimestamp() == null) {
            throw new StatInputDtoInvalidException("Проверьте переданные в статистику аргументы. Должны быть заполнены все поля.");
        }
    }
}
