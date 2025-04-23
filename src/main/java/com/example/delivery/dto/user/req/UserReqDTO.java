package com.example.delivery.dto.user.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@ToString
public class UserReqDTO {
    @NotBlank(message = "사용자 아이디는 필수 입력 항목입니다.")
    @Email(message = "이메일 형식을 입력해주세요.")
    private String email;
    @NotBlank(message = "비밀번호는 필수 값입니다.")
    @Size(min = 8, message = "8글자 이상 입력해주세요.")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*])", message = "비밀번호는 대소문자, 숫자, 특수문자를 최소 1글자씩 포함해야 합니다.")
    private String password;
    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, message = "두 글자 이상 입력해주세요.")
    private String name;
    @NotBlank(message = "권한 선택 해주세요.")
    private String role;

}
