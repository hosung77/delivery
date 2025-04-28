package com.example.delivery.service.store;

import com.example.delivery.config.error.CustomException;
import com.example.delivery.config.error.ErrorCode;
import com.example.delivery.dto.store.StoreRequestDto;
import com.example.delivery.dto.store.StoreResponseDto;
import com.example.delivery.entity.StoreEntity;
import com.example.delivery.entity.UserEntity;
import com.example.delivery.repository.store.StoreRepository;
import com.example.delivery.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

   private final StoreRepository storeRepository;
   private final UserRepository userRepository;

   // 가게 생성
   public StoreResponseDto createStore(StoreRequestDto dto) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      Long userId = (Long) authentication.getPrincipal();

      UserEntity user = userRepository.findById(userId)
              .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

      if (!user.getRoles().equals(UserEntity.Role.OWNER)) {
         throw new CustomException(ErrorCode.FORBIDDEN);
      }

      List<StoreEntity> stores = storeRepository.findByUser(user);
      if (stores.size() >= 3) {
         throw new CustomException(ErrorCode.STORE_LIMIT_EXCEEDED);
      }

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

   // 유저의 가게 목록 조회
   public List<StoreResponseDto> getStoresByUser(Long userId) {
      UserEntity user = userRepository.findById(userId)
              .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

      List<StoreEntity> stores = storeRepository.findByUser(user);

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

   // 전체 가게 목록 조회
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
                      null // 메뉴 제외
              ))
              .collect(Collectors.toList());
   }

   // 가게 단건 조회
   public StoreResponseDto getStoreById(Long storeId) {
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

   // 가게 수정
   public StoreResponseDto updateStore(Long storeId, StoreRequestDto dto, Long userId) {
      UserEntity user = userRepository.findById(userId)
              .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
      StoreEntity store = storeRepository.findById(storeId)
              .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

      if (!store.getUser().equals(user)) {
         throw new CustomException(ErrorCode.STORE_OWNER_MISMATCH);
      }

      dto.updateEntity(store);
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

   // 가게 폐업
   public StoreResponseDto closeStore(Long storeId, Long userId) {
      UserEntity user = userRepository.findById(userId)
              .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
      StoreEntity store = storeRepository.findById(storeId)
              .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

      if (!store.getUser().equals(user)) {
         throw new CustomException(ErrorCode.STORE_OWNER_MISMATCH);
      }

      store.setStatus(StoreEntity.Status.CLOSE);
      storeRepository.save(store);

      return new StoreResponseDto(
              store.getStoreId(),
              store.getName(),
              store.getStatus().toString()
      );
   }
}
