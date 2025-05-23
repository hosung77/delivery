package com.example.delivery.repository.review;

import com.example.delivery.entity.OrderEntity;
import com.example.delivery.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByStore_StoreIdAndRatingBetweenAndDeletedFalseOrderByCreatedAtDesc(Long storeId, int min, int max);
    boolean existsByOrder(OrderEntity order);
}
