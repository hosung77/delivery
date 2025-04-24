package com.example.delivery.mapper;

import com.example.delivery.dto.order.response.ResponseOrderDto;
import com.example.delivery.entity.OrderEntity;
import com.example.delivery.entity.OrderMenuEntity;
import com.example.delivery.entity.StoreEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "store", target = "store")
    @Mapping(source = "orderMenus", target = "orderItems")
    @Mapping(target = "totalAmount", expression = "java(calculateTotal(order.getOrderMenus()))")
    ResponseOrderDto toDto(OrderEntity order);

    // MapStruct가 처리 못하 총합 계산 메서드
    default int calculateTotal(List<OrderMenuEntity> orderMenus) {
        return orderMenus.stream().mapToInt(OrderMenuEntity::getTotalPrice).sum();
    }

    @Mapping(source = "storeId", target = "storeId")
    @Mapping(source = "name", target = "storeName")
    ResponseOrderDto.StoreInfo toStoreInfo(StoreEntity store);

    @Mapping(source = "menu.menuId", target = "menuId")
    @Mapping(source = "menu.name", target = "name")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(expression = "java(orderMenu.getTotalPrice())", target = "totalPrice")
    ResponseOrderDto.MenuInfo toMenuInfo(OrderMenuEntity orderMenu);

}
