package com.example.delivery.repository.menu;

import com.example.delivery.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

    //메뉴 이름 검색 (통합 검색용)
    List<MenuEntity> findByNameContaining(String keyword);

}
