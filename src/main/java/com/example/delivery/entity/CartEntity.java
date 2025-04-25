package com.example.delivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_cart")
@Getter
@NoArgsConstructor
public class CartEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;  // 주문과의 연관

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItemEntity> cartItems = new ArrayList<>();

    public CartEntity(UserEntity user) {
        this.user = user;
    }

    public void clearCart() {
        this.cartItems.clear();
    }

    public static CartEntity of(UserEntity user) {
        return new CartEntity(user);
    }

}
