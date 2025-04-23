package com.example.delivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.events.Event;

@Entity
@Table(name = "tb_session")
@Getter
@NoArgsConstructor
public class SessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true, length = 128)
    private String sessionToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private boolean isValid;

    public SessionEntity(String sessionToken, UserEntity user) {
        this.sessionToken = sessionToken;
        this.user = user;
        this.isValid = true;
    }

    public void invalidate() {
        this.isValid = false;
    }
}
