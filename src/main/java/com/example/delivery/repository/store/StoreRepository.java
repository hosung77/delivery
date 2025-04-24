package com.example.delivery.repository.store;

import com.example.delivery.entity.StoreEntity;
import com.example.delivery.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<StoreEntity, Integer> {
    // 유저에 해당하는 가게 목록 조회
    List<StoreEntity> findByUser(UserEntity user);
    // 가게 ID로 찾기
    Optional<StoreEntity> findById(int storeId);
}
