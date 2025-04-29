package com.example.delivery.controller.menu;
import com.example.delivery.dto.menu.req.CreateMenuReqDTO;
import com.example.delivery.dto.menu.req.DeleteMenuReqDTO;
import com.example.delivery.dto.menu.req.UpdateMenuReqDTO;
import com.example.delivery.dto.menu.res.UpdateMenuResDTO;
import com.example.delivery.service.menu.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/*
메뉴 단일 조회는 되지 않기에 메소드 없음
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores/menus")
public class MenuController {
    private static final Logger log = LoggerFactory.getLogger(MenuController.class);
    private final MenuService menuService;
    @PostMapping
    public ResponseEntity<Void> createMenu(
            @RequestBody @Valid CreateMenuReqDTO createMenuReqDTO,
            @AuthenticationPrincipal String userIdStr
    ) {
        Long userId = Long.parseLong(userIdStr);
        menuService.createMenu(createMenuReqDTO,userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UpdateMenuResDTO> updateMenu(
            @RequestBody @Valid UpdateMenuReqDTO updateMenuReqDTO,
            @AuthenticationPrincipal String userIdStr
    ){
        Long userId = Long.parseLong(userIdStr);
        UpdateMenuResDTO updateMenuResDTO = menuService.updateMenu(updateMenuReqDTO,userId);
        return ResponseEntity.ok(updateMenuResDTO);
    }
    // 메뉴 상태 변경
    @PatchMapping("/{menuId}/sold-out")
    public ResponseEntity<Void> markMenuAsSoldOut(
            @PathVariable Long menuId,
            @AuthenticationPrincipal String userIdStr
    ) {
        Long userId = Long.parseLong(userIdStr);
        menuService.soldOut(menuId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMenu(
            @RequestBody @Valid DeleteMenuReqDTO deleteMenuReqDTO,
            @AuthenticationPrincipal String userIdStr
    ){
        Long userId = Long.parseLong(userIdStr);
        menuService.deleteMenu(deleteMenuReqDTO,userId);
        return ResponseEntity.ok().build();
    }
}
