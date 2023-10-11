package ru.yandex.praktikum.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.praktikum.mapper.UserMapper;
import ru.yandex.praktikum.model.NewUserRequest;
import ru.yandex.praktikum.model.UserDto;
import ru.yandex.praktikum.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class EwmServiceServerAdmin {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public UserDto createUser(NewUserRequest userRequest) {
        return userRepository.save(userMapper.toUserDto(userRequest));
    }

    public List<UserDto> findUsers(List<Long> ids, Integer from, Integer size) {
        if (ids != null && !ids.isEmpty()) {
            return userRepository.findAllById(ids);
        } else {
            Pageable page = PageRequest.of(Math.abs(from / size), size);
            return userRepository.findAll(page).toList();
        }
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }
}
