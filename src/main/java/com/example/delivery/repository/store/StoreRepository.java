package com.example.delivery.repository.store;

import com.example.delivery.entity.StoreEntity;
import com.example.delivery.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    // 유저에 해당하는 가게 목록 조회
    List<StoreEntity> findByUser(UserEntity user);
    // 가게 ID로 찾기
    Optional<StoreEntity> findById(Long storeId);
    //폐업안된 가게 찾기
    List<StoreEntity> findByClosedFalse();
    //가게 이름 검색 (통합 검색용)
    List<StoreEntity> findByNameContaining(String keyword);

}
