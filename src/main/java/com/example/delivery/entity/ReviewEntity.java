package com.example.delivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_review")
@NoArgsConstructor
@Getter
public class ReviewEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String comment;

    @Column(nullable = false)
    private int rating;

    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private OrderEntity order;

    public ReviewEntity(String comment, int rating, StoreEntity store, UserEntity user, OrderEntity order) {
        this.comment = comment;
        this.rating = rating;
        this.store = store;
        this.user = user;
        this.order = order;
        this.deleted = false;
    }
}
