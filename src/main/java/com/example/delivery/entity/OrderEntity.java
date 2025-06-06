package com.example.delivery.entity;

import com.example.delivery.config.error.CustomException;
import com.example.delivery.config.error.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_order")
@Getter
@AllArgsConstructor
@Builder
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItemEntity> cartItems;


    public OrderEntity(Status status, UserEntity user, StoreEntity store, List<CartItemEntity> cartItems) {
        this.status = status;
        this.user = user;
        this.store = store;
        this.cartItems = cartItems;
    }

    public void addReview(ReviewEntity review) {
        this.review = review;
    }

    public static OrderEntity of(UserEntity user, StoreEntity store, Status status, List<CartItemEntity> cartItems) {
        return new OrderEntity(status, user, store, cartItems);
    }

    public void updateStatus(Status status) {
        // 각 상태에 따라 상태수정
        if (status == Status.ACCEPTED) {
            // status가 ACCEPTED인 경우
            this.status = Status.ACCEPTED;
        } else if (status == Status.CANCELLED) {
            // status가 ACCEPTED인 경우 처리
            this.status = Status.CANCELLED;
        } else  {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }
    }

    public void startCooking() {
        this.status = Status.COOKING;
    }

}
