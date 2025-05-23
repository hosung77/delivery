package com.example.delivery.dto.menu.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class CreateMenuReqDTO {
    @NotNull(message = "공백일 수 없습니다.")
    private Long storeId;
    @NotBlank(message = "메뉴명을 입력해주세요.")
    @Length(max = 20, message = "최대 20자 까지 입력해주세요")
    private String menuName;
    @NotNull(message = "가격을 입력해주세요.")
    private int price;
}
