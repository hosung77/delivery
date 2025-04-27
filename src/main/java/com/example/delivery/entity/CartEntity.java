package com.example.delivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_cart")
@Getter
@NoArgsConstructor
@ToString
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

    public CartEntity(UserEntity user,StoreEntity store) {
        this.user = user;
        this.store = store;
    }

    public void clearCart() {
        this.cartItems.clear();
    }

    public static CartEntity of(UserEntity user, StoreEntity store) {
        return new CartEntity(user,store);
    }

}
