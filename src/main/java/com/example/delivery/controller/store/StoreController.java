package com.example.delivery.controller.store;

import com.example.delivery.dto.store.StoreRequestDto;
import com.example.delivery.dto.store.StoreResponseDto;
import com.example.delivery.entity.UserEntity;
import com.example.delivery.service.store.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    // 가게 생성
    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(@RequestBody StoreRequestDto dto) {
        StoreResponseDto storeResponse = storeService.createStore(dto);
        return ResponseEntity.ok(storeResponse);
    }

    // 유저의 가게 목록 조회
    @GetMapping("/user")
    public ResponseEntity<List<StoreResponseDto>> getStoresByUser(@RequestAttribute UserEntity user) {
        List<StoreResponseDto> stores = storeService.getStoresByUser(user.getEmail());
        return ResponseEntity.ok(stores);
    }

    // 전체 가게 목록 조회
    @GetMapping
    public ResponseEntity<List<StoreResponseDto>> getAllStores() {
        List<StoreResponseDto> stores = storeService.getAllStores();
        return ResponseEntity.ok(stores);
    }

    // 가게 단건 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> getStoreById(@PathVariable Long storeId) {
        StoreResponseDto store = storeService.getStoreById(storeId);
        return ResponseEntity.ok(store);
    }

    // 가게 수정
    @PutMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(@PathVariable Long storeId, @RequestBody StoreRequestDto dto, @RequestAttribute UserEntity user) {
        StoreResponseDto updatedStore = storeService.updateStore(storeId, dto, user.getEmail());
        return ResponseEntity.ok(updatedStore);
    }

    // 가게 폐업
    @PutMapping("/{storeId}/close")
    public ResponseEntity<String> closeStore(@PathVariable Long storeId, @RequestAttribute UserEntity user) {
        return storeService.closeStore(storeId, user.getEmail());
    }
}
