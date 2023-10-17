package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.status.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

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

    @Query("select e " +
            "from Event e " +
            "where ((:users is null or e.initiator.id in :users) " +
            "and (:states is null or e.state in :states) " +
            "and (:categories is null or e.category.id in :categories) " +
            "and (e.eventDate between :rangeStart and :rangeEnd))")
    List<Event> findAllByAdmin(@Param("users") List<Long> users, @Param("states")List<EventState> states,
                               @Param("categories") List<Long> categories, @Param("rangeStart") LocalDateTime rangeStart,
                               @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);
}
