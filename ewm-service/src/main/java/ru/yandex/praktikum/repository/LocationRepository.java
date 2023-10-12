package ru.yandex.praktikum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.praktikum.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
