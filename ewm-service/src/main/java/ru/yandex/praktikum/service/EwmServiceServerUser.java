package ru.yandex.praktikum.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.praktikum.mapper.UserMapper;
import ru.yandex.praktikum.repository.UserRepository;

@Service
@AllArgsConstructor
public class EwmServiceServerUser {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

}
