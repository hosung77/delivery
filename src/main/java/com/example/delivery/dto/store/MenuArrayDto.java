package com.example.delivery.dto.store;

import com.example.delivery.entity.MenuEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MenuArrayDto {
    private Long menuId;
    private String name;
    private int price;
    private String status;

    public static MenuArrayDto fromEntity(MenuEntity menuEntity) {
        return MenuArrayDto.builder()
                .menuId(menuEntity.getMenuId())
                .name(menuEntity.getName())
                .price(menuEntity.getPrice())
                .status(menuEntity.getStatus().name())
                .build();
    }
}
