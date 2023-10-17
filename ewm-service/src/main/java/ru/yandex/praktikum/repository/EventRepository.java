package ru.yandex.praktikum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.praktikum.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    //todo check this usage with JPQL
    List<Event> findAllByInitiator(Long initiator);

    @Query("select e " +
            "from Event e " +
            "where (e.state = 'PUBLISHED') " +
            "and (lower(e.annotation) like lower(concat('%', :text, '%')) " +
            "or lower(e.description) like lower(concat('%', :text, '%'))) " +
            "and ((:categories) is null or e.category.id in :categories) " +
            "and ((:paid) is null or e.paid = :paid) " +
            "and (e.eventDate between :rangeStart and :rangeEnd)")
    List<Event> findAllByPublic(@Param("text") String text,
                                @Param("categories") List<Long> categories,
                                @Param("paid") Boolean paid,
                                @Param("rangeStart") LocalDateTime rangeStart,
                                @Param("rangeEnd") LocalDateTime rangeEnd,
                                Pageable page);
}
