package com.example.delivery.config.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 유저
    USER_NOT_FOUND("존재하지 않는 사용자입니다", HttpStatus.UNAUTHORIZED),
    PASSWORD_MISMATCH("비밀번호가 일치하지 않습니다", HttpStatus.UNAUTHORIZED),
    DUPLICATED_EMAIL("이미 등록된 이메일입니다.", HttpStatus.CONFLICT),
    SAME_PASSWORD("이전 비밀번호와 동일한 비밀번호로 변경할 수 없습니다.", HttpStatus.BAD_REQUEST),
    EMPTY_PROFILE_IMAGE("프로필 이미지가 비어 있습니다!", HttpStatus.BAD_REQUEST),
    IMAGE_SAVE_FAIL("이미지 저장에 실패 했습니다!", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_TOKEN("토큰이 유효하지 않습니다. 다시 로그인 해주세요", HttpStatus.UNAUTHORIZED),
    AUTH_UNAUTHORIZED("인증이 필요한 요청입니다", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("권한 없는 유저입니다.", HttpStatus.FORBIDDEN),
    BAD_REQUEST("배달 완료된 주문만 리뷰 작성 가능합니다.", HttpStatus.BAD_REQUEST),
    ALREADY_REVIEWED("이미 리뷰가 작성된 주문입니다.", HttpStatus.BAD_REQUEST),
    REVIEW_NOT_FOUND("존재하지 않는 리뷰입니다.", HttpStatus.NOT_FOUND),


    // 주문
    // 스토어
    STORE_NOT_FOUND("존재하지 않는 가게입니다.",HttpStatus.NOT_FOUND),
    STORE_NOT_AVALIABLE("현재 영업중이 아닙니다",HttpStatus.BAD_REQUEST),
    // 오더
    ORDER_NOT_FOUND("해당하는 주문을 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
    OWNER_NOT_MATCH("가게 사장님만이 주문을 관리할 수 있습니다.",HttpStatus.BAD_REQUEST),

    STORE_LIMIT_EXCEEDED("최대 3개의 가게만 등록 가능합니다.", HttpStatus.BAD_REQUEST),
    STORE_OWNER_MISMATCH("이 가게의 소유자가 아닙니다.", HttpStatus.FORBIDDEN),
    CART_NOT_FOUND("장바구니가 비어져 있습니다.", HttpStatus.NOT_FOUND),
    DIFFERENT_STORE_ITEM("같은 가게의 메뉴만 담을 수 있습니다.", HttpStatus.BAD_REQUEST),
    NOT_OVER_MINPRICE("최소 주문 금액 이상 주문을 해주셔야 합니다.", HttpStatus.BAD_REQUEST),
    KEYWORD_REQUIRED("검색어(keyword)를 입력해야 합니다.", HttpStatus.BAD_REQUEST),

    NOT_MATCH_MENU("일치하는 메뉴가 없습니다.", HttpStatus.NOT_FOUND),
    // 메뉴
    MENU_NOT_FOUND("존재하지 않는 메뉴입니다.",HttpStatus.NOT_FOUND);
    private final String message;
    private final HttpStatus status;
}
