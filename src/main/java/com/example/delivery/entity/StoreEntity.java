package com.example.delivery.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SQLRestriction("deleted_at IS NULL")
@Setter
@Builder  // 클래스에 @Builder를 한 번만 적용
@Entity
@Table(name = "tb_store")
@NoArgsConstructor
@Getter
public class StoreEntity extends BaseTimeEntity {

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
    @Builder.Default
    private List<MenuEntity> menus = new ArrayList<>();

    public StoreEntity(Long storeId, String name, LocalTime open, LocalTime close, int minOrderPrice,
                       Status status, boolean closed, UserEntity user, List<MenuEntity> menus) {
        this.storeId = storeId;
        this.name = name;
        this.open = open;
        this.close = close;
        this.minOrderPrice = minOrderPrice;
        this.status = status;
        this.closed = closed;
        this.user = user;
        this.menus = menus;
    }

    public void closed() {
        this.closed = true;
    }

    public boolean isAvailable() {
        return !closed && status == Status.OPEN;
    }
}
