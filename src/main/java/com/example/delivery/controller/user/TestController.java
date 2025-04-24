package com.example.delivery.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/api/admin/test")
    public String test() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        authentication.getName();
//        if (authentication != null) {
//            // 권한 정보 가져오기
//            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//            log.info(authorities.toString());
//            for (GrantedAuthority authority : authorities) {
//                System.out.println("권한: " + authority.getAuthority());
//            }
//        } else {
//            System.out.println("사용자가 인증되지 않았습니다.");
//        }
        return "test";
    }
    @GetMapping("/api/owner/test")
    public String test2() {
        return "test2";
    }
}
