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
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    private static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(authentication.getName());
    }

    @Transactional
    public void createMenu(CreateMenuReqDTO createMenuReqDTO,Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        StoreEntity store = storeRepository.findById(createMenuReqDTO.getStoreId())
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        System.out.println(store.getUser().getUserId() + " TEST ");
        if (!store.getUser().getUserId().equals(user.getUserId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        MenuEntity menu = MenuEntity.builder()
                .store(store)
                .name(createMenuReqDTO.getMenuName())
                .price(createMenuReqDTO.getPrice())
                .status(MenuEntity.Status.SELLING)
                .build();

        menuRepository.save(menu);
    }

    @Transactional
    public UpdateMenuResDTO updateMenu(UpdateMenuReqDTO updateMenuReqDTO, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        StoreEntity store = storeRepository.findById(updateMenuReqDTO.getStoreId())
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        if (!store.getUser().getUserId().equals(user.getUserId())) { // 스토어 사정이랑 현재 로그인 유저 번호가 같은가?
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        MenuEntity menu = menuRepository.findById(updateMenuReqDTO.getMenuId())
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
        if (!menu.getStore().getStoreId().equals(store.getStoreId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        menu.update(
                updateMenuReqDTO.getMenuName(),
                updateMenuReqDTO.getPrice(),
                MenuEntity.Status.SELLING
        );
        return UpdateMenuResDTO.builder()
                .menuId(menu.getMenuId())
                .menuName(menu.getName())
                .price(menu.getPrice())
                .status(menu.getStatus().name())
                .build();
    }

    @Transactional
    public void deleteMenu(DeleteMenuReqDTO deleteMenuReqDTO, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        StoreEntity store = storeRepository.findById(deleteMenuReqDTO.getStoreId())
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        if (!store.getUser().getUserId().equals(user.getUserId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        MenuEntity menu = menuRepository.findById(deleteMenuReqDTO.getMenuId())
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        if (!menu.getStore().getStoreId().equals(store.getStoreId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        menu.delete();
    }

    @Transactional
    public void soldOut(Long menuId, Long userId) {
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
        if (!menu.getStore().getUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        menu.soldOut();
    }
}
