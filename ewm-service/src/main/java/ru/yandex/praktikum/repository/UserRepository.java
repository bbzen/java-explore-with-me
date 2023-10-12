package ru.yandex.praktikum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.praktikum.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
