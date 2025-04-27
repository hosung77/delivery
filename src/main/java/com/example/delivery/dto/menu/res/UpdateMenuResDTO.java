package com.example.delivery.dto.menu.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class UpdateMenuResDTO {
    private Long menuId;
    private String menuName;
    private int price;
    private String status;
}
