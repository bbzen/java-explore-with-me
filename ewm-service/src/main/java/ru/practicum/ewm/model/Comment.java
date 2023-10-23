package ru.practicum.ewm.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comments", schema = "public")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    private User author;
    @ManyToOne
    @JoinColumn(name = "event", nullable = false)
    private Event event;
    private String text;
    private LocalDateTime timestamp;

    public Comment() {
        this.timestamp = LocalDateTime.now();
    }

    public Comment(Long id, User author, Event event, String text) {
        this.id = id;
        this.author = author;
        this.event = event;
        this.text = text;
        this.timestamp = LocalDateTime.now();
    }
}
