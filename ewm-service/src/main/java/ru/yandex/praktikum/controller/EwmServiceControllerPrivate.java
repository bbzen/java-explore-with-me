package ru.yandex.praktikum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.model.dto.EventFullDto;
import ru.yandex.praktikum.model.dto.NewEventDto;
import ru.yandex.praktikum.model.dto.UpdateEventUserRequest;
import ru.yandex.praktikum.service.EwmServiceServerPrivate;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class EwmServiceControllerPrivate {
    @Autowired
    private EwmServiceServerPrivate serverPrivate;

    @GetMapping("/{userId}/events")
    public List<EventFullDto> findAllUserEvents(@PathVariable Long userId) {
        return serverPrivate.findAllUserEvents(userId);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto dto) {
        return serverPrivate.createEvent(userId, dto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto findUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return serverPrivate.findUserEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId, @PathVariable Long eventId, @RequestBody UpdateEventUserRequest dto) {
        return serverPrivate.updateEvent(userId, eventId, dto);
    }
}
