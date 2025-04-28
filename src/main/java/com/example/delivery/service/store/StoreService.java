package com.example.delivery.service.store;

import com.example.delivery.config.error.CustomException;
import com.example.delivery.config.error.ErrorCode;
import com.example.delivery.dto.store.StoreRequestDto;
import com.example.delivery.dto.store.StoreResponseDto;
import com.example.delivery.entity.StoreEntity;
import com.example.delivery.entity.UserEntity;
import com.example.delivery.repository.store.StoreRepository;
import com.example.delivery.repository.user.UserRepository;
import com.example.delivery.service.auth.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

   private final StoreRepository storeRepository;
   private final UserRepository userRepository;

   // 가게 생성 서비스
   public StoreResponseDto createStore(StoreRequestDto dto) {
      Long userId = SecurityUtil.getCurrentUserId();  // JWT로 로그인된 유저 ID 가져오기

      UserEntity user = userRepository.findById(userId)
              .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

      // 사장님 권한 확인
      if (!user.getRoles().equals(UserEntity.Role.OWNER)) {
         throw new CustomException(ErrorCode.FORBIDDEN);
      }

      // 최대 3개의 가게 제한
      List<StoreEntity> stores = storeRepository.findByUser(user);
      if (stores.size() >= 3) {
         throw new CustomException(ErrorCode.STORE_LIMIT_EXCEEDED);
      }

      // StoreEntity 생성 및 저장
      StoreEntity store = StoreEntity.builder()
              .name(dto.getName())
              .open(LocalTime.parse(dto.getOpen()))
              .close(LocalTime.parse(dto.getClose()))
              .minOrderPrice(dto.getMinOrderPrice())
              .status(StoreEntity.Status.OPEN)
              .closed(false)
              .user(user)
              .build();

      StoreEntity savedStore = storeRepository.save(store);

      return new StoreResponseDto(
              savedStore.getStoreId(),
              savedStore.getName(),
              savedStore.getOpen(),
              savedStore.getClose(),
              savedStore.getMinOrderPrice(),
              savedStore.getStatus().toString(),
              savedStore.getMenus()
      );
   }

   // 가게 수정 서비스
   public StoreResponseDto updateStore(Long storeId, StoreRequestDto dto) {
      Long userId = SecurityUtil.getCurrentUserId();  // JWT로 로그인된 유저 ID 가져오기

      // 유저 정보 조회
      UserEntity user = userRepository.findById(userId)
              .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

      // 가게 정보 조회
      StoreEntity store = storeRepository.findById(storeId)
              .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

      // 유저가 해당 가게의 주인이 맞는지 확인
      if (!store.getUser().equals(user)) {
         throw new CustomException(ErrorCode.STORE_OWNER_MISMATCH);
      }

      // DTO를 이용한 가게 정보 업데이트
      dto.updateEntity(store);

      // 저장 후 응답 DTO 반환
      StoreEntity updatedStore = storeRepository.save(store);
      return new StoreResponseDto(
              updatedStore.getStoreId(),
              updatedStore.getName(),
              updatedStore.getOpen(),
              updatedStore.getClose(),
              updatedStore.getMinOrderPrice(),
              updatedStore.getStatus().toString(),
              updatedStore.getMenus()
      );
   }

   // 가게 폐업 서비스
   public StoreResponseDto closeStore(Long storeId) {
      Long userId = SecurityUtil.getCurrentUserId();  // JWT로 로그인된 유저 ID 가져오기

      // 유저 정보 조회
      UserEntity user = userRepository.findById(userId)
              .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

      // 가게 정보 조회
      StoreEntity store = storeRepository.findById(storeId)
              .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

      // 유저가 해당 가게의 주인이 맞는지 확인
      if (!store.getUser().equals(user)) {
         throw new CustomException(ErrorCode.STORE_OWNER_MISMATCH);
      }

      // 가게 상태 변경 (폐업)
      store.setStatus(StoreEntity.Status.CLOSE);
      storeRepository.save(store);

      return new StoreResponseDto(store.getStoreId(), store.getName(), store.getStatus().name());
   }

   // 유저의 가게 목록 조회 서비스
   public List<StoreResponseDto> getStoresByUser() {
      Long userId = SecurityUtil.getCurrentUserId();  // JWT로 로그인된 유저 ID 가져오기

      // 유저 정보 조회
      UserEntity user = userRepository.findById(userId)
              .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

      // 해당 유저의 가게 목록 조회
      List<StoreEntity> stores = storeRepository.findByUser(user);

      // StoreResponseDto로 변환하여 반환
      return stores.stream()
              .map(store -> new StoreResponseDto(
                      store.getStoreId(),
                      store.getName(),
                      store.getOpen(),
                      store.getClose(),
                      store.getMinOrderPrice(),
                      store.getStatus().toString(),
                      store.getMenus()
              ))
              .collect(Collectors.toList());
   }

   // 모든 가게 목록 조회 서비스
   public List<StoreResponseDto> getAllStores() {
      List<StoreEntity> stores = storeRepository.findAll();

      return stores.stream()
              .map(store -> new StoreResponseDto(
                      store.getStoreId(),
                      store.getName(),
                      store.getOpen(),
                      store.getClose(),
                      store.getMinOrderPrice(),
                      store.getStatus().toString(),
                      store.getMenus()
              ))
              .collect(Collectors.toList());
   }

   // 특정 가게 정보 조회 서비스 (추가된 메서드)
   public StoreResponseDto getStoreById(Long storeId) {
      // 가게 정보 조회
      StoreEntity store = storeRepository.findById(storeId)
              .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

      return new StoreResponseDto(
              store.getStoreId(),
              store.getName(),
              store.getOpen(),
              store.getClose(),
              store.getMinOrderPrice(),
              store.getStatus().toString(),
              store.getMenus()
      );
   }
}
