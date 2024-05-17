package com.jobmatchup.jobmatchup.entities.chat;

import java.time.LocalDateTime;
import com.jobmatchup.jobmatchup.entities.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CHAT_TBL")
@Setter
@Getter
@ToString
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SENDER_ID", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "RECEIVER_ID", nullable = false)
    private User receiver;

    @Column(name = "MESSAGE", nullable = false, length = 1000)
    private String message;

    @Column(name = "TIMESTAMP", nullable = false)
    private LocalDateTime timestamp;

    public Chat() {
    }

    public Chat(User sender, User receiver, String message, LocalDateTime timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
    } 
}
