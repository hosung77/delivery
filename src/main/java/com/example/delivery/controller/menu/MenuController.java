package com.example.delivery.controller.menu;

import com.example.delivery.dto.menu.req.CreateMenuReqDTO;
import com.example.delivery.dto.menu.req.DeleteMenuReqDTO;
import com.example.delivery.dto.menu.req.UpdateMenuReqDTO;
import com.example.delivery.dto.menu.res.UpdateMenuResDTO;
import com.example.delivery.service.menu.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/*
메뉴 단일 조회는 되지 않기에 메소드 없음
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/menus")
public class MenuController {
    private final MenuService menuService;
    @PostMapping
    public ResponseEntity<Void> createMenu(
            @RequestBody @Valid CreateMenuReqDTO createMenuReqDTO
    ) {
        menuService.createMenu(createMenuReqDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UpdateMenuResDTO> updateMenu(
            @RequestBody @Valid UpdateMenuReqDTO updateMenuReqDTO
    ){
        UpdateMenuResDTO updateMenuResDTO = menuService.updateMenu(updateMenuReqDTO);
        return ResponseEntity.ok(updateMenuResDTO);
    }
    // 메뉴 상태 변경
    @PatchMapping("/{menuId}/sold-out")
    public ResponseEntity<Void> markMenuAsSoldOut(@PathVariable Long menuId) {
        menuService.soldOut(menuId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMenu(
            @RequestBody @Valid DeleteMenuReqDTO deleteMenuReqDTO
    ){
        menuService.deleteMenu(deleteMenuReqDTO);
        return ResponseEntity.ok().build();
    }
}
