package ru.yandex.praktikum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.praktikum.model.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
}
