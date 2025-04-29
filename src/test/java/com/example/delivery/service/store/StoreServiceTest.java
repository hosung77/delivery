package com.example.delivery.service.store;

import com.example.delivery.config.error.CustomException;
import com.example.delivery.dto.store.StoreRequestDto;
import com.example.delivery.dto.store.StoreResponseDto;
import com.example.delivery.entity.StoreEntity;
import com.example.delivery.entity.UserEntity;
import com.example.delivery.repository.store.StoreRepository;
import com.example.delivery.repository.user.UserRepository;
import com.example.delivery.service.store.StoreService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.delivery.config.error.ErrorCode;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Transactional
class StoreServiceTest {

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    private UserEntity owner;
    private UserEntity nonOwner;

    @BeforeEach
    void setUp() {
        // DB 초기화
        storeRepository.deleteAll();
        userRepository.deleteAll();

        // 유저 생성: 사장님
        owner = UserEntity.builder()
                .email("owner@example.com")
                .password("1234")
                .name("사장님")
                .roles(UserEntity.Role.OWNER)
                .build();
        userRepository.save(owner);

        // 유저 생성: 일반 사용자
        nonOwner = UserEntity.builder()
                .email("user@example.com")
                .password("5678")
                .name("일반 사용자")
                .roles(UserEntity.Role.USER)
                .build();
        userRepository.save(nonOwner);

        // 가게 여러 개 저장
        StoreEntity store1 = StoreEntity.builder()
                .name("김밥천국")
                .open(LocalTime.of(9, 0))
                .close(LocalTime.of(22, 0))
                .minOrderPrice(10000)
                .status(StoreEntity.Status.OPEN)
                .closed(false)
                .user(owner)
                .build();

        StoreEntity store2 = StoreEntity.builder()
                .name("불고기브라더스")
                .open(LocalTime.of(10, 0))
                .close(LocalTime.of(20, 0))
                .minOrderPrice(15000)
                .status(StoreEntity.Status.OPEN)
                .closed(false)
                .user(owner)
                .build();

        StoreEntity store3 = StoreEntity.builder()
                .name("치킨마을")
                .open(LocalTime.of(11, 0))
                .close(LocalTime.of(23, 0))
                .minOrderPrice(12000)
                .status(StoreEntity.Status.CLOSE)  // 폐업된 가게
                .closed(true)
                .user(owner)
                .build();

        storeRepository.saveAll(List.of(store1, store2, store3));
    }

    @Test
    void 전체_가게_조회_성공() {
        // when
        List<StoreResponseDto> result = storeService.getAllStores();

        // then
        assertThat(result).hasSize(2); // 폐업된 가게는 제외되어야 하므로 두 개의 가게만 반환됨
        assertThat(result.get(0).getName()).isEqualTo("김밥천국");
        assertThat(result.get(1).getName()).isEqualTo("불고기브라더스");

        // 결과 출력
        System.out.println("조회된 가게 목록: ");
        result.forEach(store -> System.out.println(store.getName()));
    }

    @Test
    void 사장이_아닌_사용자가_가게_생성_시_예외_발생() {
        // given
        StoreRequestDto storeRequestDto = new StoreRequestDto("테스트 가게", "09:00", "22:00", 10000);

        // when & then
        assertThatThrownBy(() -> storeService.createStore(storeRequestDto, nonOwner.getUserId()))  // userId 직접 전달
                .isInstanceOf(CustomException.class)  // 예외 타입 확인
                .hasMessage(ErrorCode.FORBIDDEN.getMessage());  // 에러 메시지 확인
    }

}
