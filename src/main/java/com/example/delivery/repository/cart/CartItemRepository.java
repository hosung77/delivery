package com.example.delivery.repository.cart;

import com.example.delivery.entity.CartEntity;
import com.example.delivery.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    void deleteAllByCart(CartEntity cart);

    List<CartItemEntity> findByCart(CartEntity cart);  // userId 기준으로 검색
}
