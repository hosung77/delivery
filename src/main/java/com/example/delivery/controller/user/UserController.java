package com.example.delivery.controller.user;
import com.example.delivery.dto.user.req.UserReqDTO;
import com.example.delivery.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    @PostMapping
    @ResponseBody
    public ResponseEntity<Void> createUser(
            @RequestBody UserReqDTO user) {
        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/test")
    public ResponseEntity<String> adminOnlyApi() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // 권한 정보 가져오기
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            log.info(authorities.toString());
            for (GrantedAuthority authority : authorities) {
                System.out.println("권한: " + authority.getAuthority());
            }
        } else {
            System.out.println("사용자가 인증되지 않았습니다.");
        }

        return ResponseEntity.ok("Owner access granted");
    }




}
