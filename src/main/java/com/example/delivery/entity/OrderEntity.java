package com.example.delivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_order")
@Getter
@NoArgsConstructor
public class OrderEntity extends BaseTimeEntity{
    public enum Status {
        ORDERED, ACCEPTED, COOKING, CANCELLED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private ReviewEntity review;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderMenuEntity> orderMenus = new ArrayList<>();


    public OrderEntity(Status status, UserEntity user, StoreEntity store) {
        this.status = status;
        this.user = user;
        this.store = store;
    }
    public void addReview(ReviewEntity review) {
        this.review = review;
    }

    public static OrderEntity of(UserEntity user, StoreEntity store, Status status) {
       return new OrderEntity(status, user, store);
    }

}
