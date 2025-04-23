package com.example.delivery.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestDto {
    //가게 등록시 필요한 이름 개점 폐점 최소주문 금액
    private String name;
    private String open;
    private String close;
    private int minOrderPrice;


}
