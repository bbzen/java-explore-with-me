package ru.yandex.praktikum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.praktikum.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
