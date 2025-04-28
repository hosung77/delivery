package com.example.delivery.dto.cart.response;

import lombok.Getter;

@Getter
public class CartResponseDto {
    private String message;

    // 메시지만 담는 생성자
    public CartResponseDto(String message) {
        this.message = message;
    }

    // 장바구니 항목 추가 성공 메시지
    public static CartResponseDto itemAdded() {
        return new CartResponseDto("메뉴가 성공적으로 추가되었습니다.");
    }

    // 장바구니 항목 삭제 성공 메시지
    public static CartResponseDto itemDeleted() {
        return new CartResponseDto("장바구니 항목이 삭제되었습니다.");
    }

    // 장바구니 초기화 성공 메시지
    public static CartResponseDto cartCleared() {
        return new CartResponseDto("장바구니가 초기화되었습니다.");
    }
}