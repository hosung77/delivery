package com.example.delivery.service.search;

import com.example.delivery.dto.search.SearchResponseDTO;
import com.example.delivery.entity.MenuEntity;
import com.example.delivery.entity.StoreEntity;
import com.example.delivery.repository.menu.MenuRepository;
import com.example.delivery.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    // 인기 검색어 저장용 (메모리 캐시)
    private final Map<String, Integer> keywordCount = new ConcurrentHashMap<>();

    public SearchResponseDTO search(String keyword) {
        // 검색어 기록
        keywordCount.merge(keyword, 1, Integer::sum);

        // 가게 검색
        List<SearchResponseDTO.StoreDTO> stores = storeRepository.findByNameContaining(keyword)
                .stream()
                .filter(store -> !store.isClosed())
                .map(store -> new SearchResponseDTO.StoreDTO(store.getStoreId(), store.getName()))
                .collect(Collectors.toList());

        // 메뉴 검색
        List<SearchResponseDTO.MenuDTO> menus = menuRepository.findByNameContaining(keyword)
                .stream()
                .filter(menu -> menu.getStatus() != MenuEntity.Status.SOLD_OUT)
                .map(menu -> new SearchResponseDTO.MenuDTO(menu.getMenuId(), menu.getName()))
                .collect(Collectors.toList());

        return new SearchResponseDTO(stores, menus);
    }

    // 인기 검색어 상위 N개 반환
    public List<String> getPopularKeywords(int limit) {
        return keywordCount.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // 내림차순
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
