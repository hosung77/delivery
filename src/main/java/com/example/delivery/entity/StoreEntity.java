package com.example.delivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_store")
@NoArgsConstructor
@Getter
public class StoreEntity extends BaseTimeEntity{
    public enum Status {
        OPEN, CLOSE
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(nullable = false, length = 10, unique = true)
    private String name;

    @Column(nullable = false)
    private LocalTime open;
    @Column(nullable = false)
    private LocalTime close;
    @Column(nullable = false)
    private int minOrderPrice;

    @Enumerated(EnumType.STRING)
    private Status status; // 가게 열린지 닫혔는지 상태

    private boolean closed; // 폐업
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<MenuEntity> menus = new ArrayList<>();

    public StoreEntity(String name, LocalTime open, LocalTime close, int minOrderPrice, Status status, boolean closed, UserEntity user) {
        this.name = name;
        this.open = open;
        this.close = close;
        this.minOrderPrice = minOrderPrice;
        this.status = status;
        this.closed = closed;
        this.user = user;
    }

    public void closed(){
        this.closed = true;
    }
    public boolean isAvailable() {
        return !closed && status == Status.OPEN;
    }

}
