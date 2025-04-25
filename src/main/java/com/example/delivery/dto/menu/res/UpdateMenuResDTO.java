package com.example.delivery.dto.menu.res;

import lombok.Builder;

@Builder
public class UpdateMenuResDTO {
    private Long menuId;
    private String menuName;
    private int price;
    private String status;
}
