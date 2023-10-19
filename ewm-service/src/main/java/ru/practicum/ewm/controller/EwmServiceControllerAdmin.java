package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.dto.*;
import ru.practicum.ewm.model.status.EventState;
import ru.practicum.ewm.service.EwmServiceServerAdmin;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class EwmServiceControllerAdmin {
    @Autowired
    private EwmServiceServerAdmin serviceServerAdmin;
    private final static String DATETIMEPATTERN = "yyyy-MM-dd HH:mm:ss";

    @GetMapping("/users")
    public List<UserDto> getUsers(
            @RequestParam List<Long> ids,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return serviceServerAdmin.findUsers(ids, from, size);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest userRequest) {
        return serviceServerAdmin.createUser(userRequest);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        serviceServerAdmin.deleteUser(userId);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto categoryDto) {
        return serviceServerAdmin.createCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        serviceServerAdmin.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto dto, @PathVariable Long catId) {
        return serviceServerAdmin.updateCategory(dto, catId);
    }

    @GetMapping("/events")
    public List<EventFullDto> findAllEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATETIMEPATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATETIMEPATTERN) LocalDateTime rangeEnd,
            @Valid @RequestParam(defaultValue = "0") @Min(0) int from,
            @Valid @RequestParam(defaultValue = "10") @Min(1) int size) {
        return serviceServerAdmin.findAllEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return serviceServerAdmin.updateEvent(eventId, updateEventAdminRequest);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return serviceServerAdmin.createCompilation(newCompilationDto);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(
            @PathVariable Long compId,
            @Valid @RequestBody UpdateCompilationRequest compilationRequest) {
        return serviceServerAdmin.updateCompilation(compId, compilationRequest);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        serviceServerAdmin.deleteCompilation(compId);
    }
}
