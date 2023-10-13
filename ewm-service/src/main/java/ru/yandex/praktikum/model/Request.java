package ru.yandex.praktikum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.praktikum.model.status.ParticipationRequestStatus;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "requests", schema = "public")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String created;
    @ManyToOne
    private Event event;
    @ManyToOne
    private User requester;
    private ParticipationRequestStatus status;
}
