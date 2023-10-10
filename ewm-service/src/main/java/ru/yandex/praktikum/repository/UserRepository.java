package ru.yandex.praktikum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.praktikum.model.UserDto;

public interface UserRepository extends JpaRepository<UserDto, Long> {

}
