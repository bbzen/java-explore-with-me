package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Request;
import ru.practicum.model.status.RequestStatus;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByEventId(Long eventId);

    Long countByEventIdAndStatus(Long eventId, RequestStatus status);

    List<Request> findAllByIdIn(List<Long> ids);

    List<Request> findAllByRequesterId(Long userId);
}
