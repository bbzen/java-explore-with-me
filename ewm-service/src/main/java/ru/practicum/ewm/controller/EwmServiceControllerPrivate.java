package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.dto.*;
import ru.practicum.ewm.service.EwmServiceServerPrivate;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class EwmServiceControllerPrivate {
    @Autowired
    private EwmServiceServerPrivate serverPrivate;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> findAllUserEvents(@PathVariable Long userId,
                                                 @Valid @RequestParam(defaultValue = "0") @Min(0) int from,
                                                 @Valid @RequestParam(defaultValue = "10") @Min(1) int size) {
        return serverPrivate.findAllUserEvents(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto findUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return serverPrivate.findUserEvent(userId, eventId);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto dto) {
        return serverPrivate.createEvent(userId, dto);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventUserRequest dto) {
        return serverPrivate.updateEvent(userId, eventId, dto);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> findAllRequests(@PathVariable Long userId) {
        return serverPrivate.findAllRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return serverPrivate.createRequest(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> findAllParticipationRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        return serverPrivate.findAllRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateParticipationRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequest dto) {
        return serverPrivate.updateRequest(userId, eventId, dto);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto updateRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return serverPrivate.updateRequestToCanceled(userId, requestId);
    }

    @GetMapping("/{userId}/comments/{commentId}")
    public CommentDto findUserComment(@PathVariable Long userId, @PathVariable Long commentId) {
        return serverPrivate.findUserComment(userId, commentId);
    }

    @PostMapping("/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Long userId, @RequestParam Long eventId, @Valid @RequestBody NewCommentDto dto) {
        return serverPrivate.createComment(userId, eventId, dto);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public CommentDto updateComment(
            @PathVariable Long userId,
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentDto dto) {
        return serverPrivate.updateComment(userId, commentId, dto);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        serverPrivate.deleteComment(userId, commentId);
    }
}
