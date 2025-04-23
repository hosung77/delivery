package com.example.delivery.controller;

import com.example.delivery.dto.store.StoreRequestDto;
import com.example.delivery.dto.store.StoreResponseDto;
import com.example.delivery.service.store.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    // 가게 생성
    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(@RequestBody StoreRequestDto dto, @RequestParam String email) {
        StoreResponseDto storeResponse = storeService.createStore(dto, email);
        return ResponseEntity.ok(storeResponse);
    }

    // 유저의 가게 목록 조회
    @GetMapping("/user")
    public ResponseEntity<List<StoreResponseDto>> getStoresByUser(@RequestParam String email) {
        List<StoreResponseDto> stores = storeService.getStoresByUser(email);
        return ResponseEntity.ok(stores);
    }

    // 가게 단건 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> getStoreById(@PathVariable int storeId) {
        StoreResponseDto store = storeService.getStoreById(storeId);
        return ResponseEntity.ok(store);
    }

    // 가게 수정
    @PutMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(@PathVariable int storeId, @RequestBody StoreRequestDto dto, @RequestParam String email) {
        StoreResponseDto updatedStore = storeService.updateStore(storeId, dto, email);
        return ResponseEntity.ok(updatedStore);
    }

    // 가게 폐업
    @PutMapping("/{storeId}/close")
    public ResponseEntity<String> closeStore(@PathVariable int storeId, @RequestParam String email) {
        String response = storeService.closeStore(storeId, email);
        return ResponseEntity.ok(response);
    }
}
