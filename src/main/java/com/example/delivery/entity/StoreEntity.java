package com.example.delivery.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "tb_store")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Builder  // 클래스에 @Builder를 한 번만 적용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static StoreEntity of(){
        return new StoreEntity();
    }

    public StoreEntity(String name, LocalTime open, LocalTime close, int minOrderPrice,
                       Status status, boolean closed, UserEntity user, List<MenuEntity> menus) {
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

    public boolean isOwner(Long userId) {
        return this.user != null && this.user.getUserId().equals(userId);
    }

    // 폐업인지 아닌지 영업중인지 아닌지에 관한 메서드
    public boolean isOperating() {
        return this.status == Status.OPEN;
    }

    public boolean isSameStore(StoreEntity storeEntity) {
        return this.storeId.equals(storeEntity.getStoreId());
    }

    // 최소 주문 금액 확인하는 메서드
    public boolean isMinOrderPrice(int price) {
        return price >= minOrderPrice;
    }

    public void updateMinOrderPrice(int newMinOrderPrice) {
        this.minOrderPrice = newMinOrderPrice;
    }
}
