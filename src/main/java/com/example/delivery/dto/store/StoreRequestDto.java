package com.example.delivery.dto.store;

import com.example.delivery.entity.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestDto {
    //가게 등록시 필요한 이름 개점 폐점 최소주문 금액
    private String name;
    private String open;
    private String close;
    private int minOrderPrice;

    public void updateEntity(StoreEntity store) {
        store.setName(this.name);
        store.setOpen(LocalTime.parse(this.open));
        store.setClose(LocalTime.parse(this.close));
        store.setMinOrderPrice(this.minOrderPrice);
    }

}
