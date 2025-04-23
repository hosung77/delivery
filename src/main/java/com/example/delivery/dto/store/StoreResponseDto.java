package com.example.delivery.dto.store;

import com.example.delivery.entity.MenuEntity;
import lombok.Getter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class StoreResponseDto {
    private int id;
    private String name;
    private String open; //응답에서는 String 사용
    private String close;
    private int minOrderPrice;
    private String status;
    private List<MenuEntity> menus;

    // DateTimeFormatter를 static으로 정의하여 재사용 가능하게 설정
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    public StoreResponseDto(int id, String name, LocalTime open, LocalTime close, int minOrderPrice,  String status, List<MenuEntity> menus) {
        this.id = id;
        this.name = name;

        // LocalTime을 "HH:mm" 형식의 String으로 변환 초부분을 빼기위함
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.open = open.format(formatter);
        this.close =close.format(formatter);

        this.minOrderPrice = minOrderPrice;
        this.status = status;
        this.menus = menus;
    }

}
