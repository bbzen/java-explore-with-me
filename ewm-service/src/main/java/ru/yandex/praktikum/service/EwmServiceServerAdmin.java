package ru.yandex.praktikum.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.praktikum.exception.ValidationException;
import ru.yandex.praktikum.mapper.UserMapper;
import ru.yandex.praktikum.model.NewUserRequest;
import ru.yandex.praktikum.model.UserDto;
import ru.yandex.praktikum.repository.UserRepository;

@Service
@AllArgsConstructor
public class EwmServiceServerAdmin {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public UserDto createUser(NewUserRequest userRequest) {
        checkNewUserRequest(userRequest);
        return userRepository.save(userMapper.toUserDto(userRequest));
    }

    private void checkNewUserRequest(NewUserRequest nur) {
        if (nur.getEmail() == null || nur.getEmail().isBlank() || !nur.getEmail().contains("@") || nur.getEmail().length() < 6 || 254 < nur.getEmail().length()) {
            throw new ValidationException("Адрес электронной почты не может быть пустой и должен содержать символ @");
        }
        if (nur.getName() == null || nur.getName().isBlank() ||  nur.getName().length() < 2 || 250 < nur.getName().length()) {
            throw new ValidationException("Имя дб больше 2 и меньше 250 символов.");
        }
    }
}
