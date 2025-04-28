package com.example.delivery.service.menu;

import com.example.delivery.config.error.CustomException;
import com.example.delivery.config.error.ErrorCode;
import com.example.delivery.dto.menu.req.CreateMenuReqDTO;
import com.example.delivery.dto.menu.req.DeleteMenuReqDTO;
import com.example.delivery.dto.menu.req.UpdateMenuReqDTO;
import com.example.delivery.dto.menu.res.UpdateMenuResDTO;
import com.example.delivery.entity.MenuEntity;
import com.example.delivery.entity.StoreEntity;
import com.example.delivery.entity.UserEntity;
import com.example.delivery.repository.menu.MenuRepository;
import com.example.delivery.repository.store.StoreRepository;
import com.example.delivery.repository.user.UserRepository;
import org.antlr.v4.runtime.misc.LogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;

import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class MenuServiceTest {
    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void 메뉴_생성_성공() {
        // given
        CreateMenuReqDTO reqDTO = new CreateMenuReqDTO(1L, "Test Menu", 10000);


        UserEntity user = UserEntity.builder().userId(1L).build();
        StoreEntity store = StoreEntity.builder().storeId(1L).user(user).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        // when
        menuService.createMenu(reqDTO, 1L);

    }


    @Test
    void 메뉴_수정_성공() {
        // given
        UpdateMenuReqDTO reqDTO = new UpdateMenuReqDTO(1L, 1L, "Updated Menu", 12000);

        UserEntity user = UserEntity.builder().userId(1L).build();
        StoreEntity store = StoreEntity.builder().storeId(1L).user(user).build();
        MenuEntity menu = MenuEntity.builder().menuId(1L).store(store).name("Old Menu").price(10000).status(MenuEntity.Status.SELLING).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        // when
        UpdateMenuResDTO resDTO = menuService.updateMenu(reqDTO, 1L);

        // then
        assertThat(resDTO.getMenuName()).isEqualTo("Updated Menu");
        assertThat(resDTO.getPrice()).isEqualTo(12000);
    }

    @Test
    void 메뉴_삭제_성공() {
        // given
        DeleteMenuReqDTO reqDTO = new DeleteMenuReqDTO(1L, 1L);

        UserEntity user = UserEntity.builder().userId(1L).build();
        StoreEntity store = StoreEntity.builder().storeId(1L).user(user).build();
        MenuEntity menu = MenuEntity.builder().menuId(1L).store(store).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        // when
        menuService.deleteMenu(reqDTO, 1L);

        // then
        assertThat(menu.getStatus()).isEqualTo(MenuEntity.Status.DELETED);
    }

    @Test
    void 메뉴_품절처리_성공() {
        // given
        UserEntity user = UserEntity.builder().userId(1L).build();
        StoreEntity store = StoreEntity.builder().storeId(1L).user(user).build();
        MenuEntity menu = MenuEntity.builder().menuId(1L).store(store).build();

        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        // when
        menuService.soldOut(1L, 1L);

        // then
        assertThat(menu.getStatus()).isEqualTo(MenuEntity.Status.SOLD_OUT);
    }

    @Test
    void 본인_아닌_가게_메뉴_수정_시_예외발생() {
        // given
        UpdateMenuReqDTO reqDTO = new UpdateMenuReqDTO(1L, 1L, "Updated Menu", 12000);

        UserEntity otherUser = UserEntity.builder().userId(2L).build();
        StoreEntity store = StoreEntity.builder().storeId(1L).user(otherUser).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(UserEntity.builder().userId(1L).build()));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        // when & then
        assertThatThrownBy(() -> menuService.updateMenu(reqDTO, 1L))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.FORBIDDEN.getMessage());
    }

}
