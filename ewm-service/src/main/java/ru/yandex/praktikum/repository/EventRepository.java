package ru.yandex.praktikum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.praktikum.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}