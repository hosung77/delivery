package com.example.delivery.service.store;

import com.example.delivery.dto.store.StoreRequestDto;
import com.example.delivery.dto.store.StoreResponseDto;
import com.example.delivery.entity.StoreEntity;
import com.example.delivery.entity.UserEntity;
import com.example.delivery.repository.store.StoreRepository;
import com.example.delivery.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {

   private final StoreRepository storeRepository;
   private final UserRepository userRepository;

   public StoreService(StoreRepository storeRepository, UserRepository userRepository) {
      this.storeRepository = storeRepository;
      this.userRepository = userRepository;
   }

   // 가게 생성 서비스
   public StoreResponseDto createStore(StoreRequestDto dto, String email) {
      // 1. 유저 정보 조회 (사장님 권한 확인)
      UserEntity user = userRepository.findByEmail(email)
              .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

      // 2. 사장님 권한 확인
      if (!user.getRoles().equals(UserEntity.Role.OWNER)) {
         throw new RuntimeException("사장인 유저만 가게 생성이 가능합니다.");
      }

      // 3. 최대 3개의 가게 제한
      List<StoreEntity> stores = storeRepository.findByUser(user);
      if (stores.size() >= 3) {
         throw new IllegalArgumentException("최대 3개의 가게만 등록 가능합니다.");
      }

      // 4. StoreEntity 생성 (Builder 활용)
      StoreEntity store = StoreEntity.builder()
              .name(dto.getName())
              .open(LocalTime.parse(dto.getOpen()))  // String -> LocalTime
              .close(LocalTime.parse(dto.getClose())) // String -> LocalTime
              .minOrderPrice(dto.getMinOrderPrice())
              .status(StoreEntity.Status.OPEN)  // 기본 상태는 OPEN
              .closed(false)  // 기본값은 false
              .user(user)  // 가게 주인 설정
              .build();

      // 5. 가게 저장
      StoreEntity savedStore = storeRepository.save(store);

      // 6. 응답 DTO로 변환하여 반환
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

   // 가게 목록 조회 서비스 (유저의 가게 목록 조회)
   public List<StoreResponseDto> getStoresByUser(String email) {
      // 1. 유저 정보 조회
      UserEntity user = userRepository.findByEmail(email)
              .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

      // 2. 해당 유저의 가게 목록 조회
      List<StoreEntity> stores = storeRepository.findByUser(user);

      // 3. StoreResponseDto로 변환하여 반환
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

   // 가게 단건 조회 서비스 (가게 상세 조회)
   public StoreResponseDto getStoreById(int storeId) {
      // 1. 가게 정보 조회
      StoreEntity store = storeRepository.findById(storeId)
              .orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));

      // 2. StoreResponseDto로 변환하여 반환
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

   // 가게 수정 서비스
   public StoreResponseDto updateStore(int storeId, StoreRequestDto dto, String email) {
      // 1. 유저 정보 조회 (사장님 권한 확인)
      UserEntity user = userRepository.findByEmail(email)
              .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

      // 2. 가게 정보 조회
      StoreEntity store = storeRepository.findById(storeId)
              .orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));

      // 3. 유저가 해당 가게의 주인이 맞는지 확인
      if (!store.getUser().equals(user)) {
         throw new RuntimeException("이 가게는 이 사용자가 소유한 가게가 아닙니다.");
      }

      // 4. 가게 수정
      store.setName(dto.getName());
      store.setOpen(LocalTime.parse(dto.getOpen()));
      store.setClose(LocalTime.parse(dto.getClose()));
      store.setMinOrderPrice(dto.getMinOrderPrice());

      // 5. 저장 후 응답 DTO 반환
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
   public String closeStore(int storeId, String email) {
      // 1. 유저 정보 조회 (사장님 권한 확인)
      UserEntity user = userRepository.findByEmail(email)
              .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

      // 2. 해당 유저의 가게 조회
      StoreEntity store = storeRepository.findById(storeId)
              .orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));

      // 3. 유저가 해당 가게의 주인이 맞는지 확인
      if (!store.getUser().equals(user)) {
         throw new RuntimeException("이 가게는 이 유저가 소유한 가게가 아닙니다.");
      }

      // 4. 가게 상태 변경 (폐업)
      store.setStatus(StoreEntity.Status.CLOSE);
      storeRepository.save(store);

      return "가게가 폐업되었습니다.";
   }
}
