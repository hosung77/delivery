package com.example.delivery.config.filter;

import com.example.delivery.config.error.CustomException;
import com.example.delivery.config.error.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
public class RoleFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        logger.info("method: " + method);
        logger.info("path: " + path);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        boolean isOwner = isAuthenticated && authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_OWNER"));
        boolean isUser = isAuthenticated && authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_USER"));

        Map<String, Set<String>> ownerPaths = new HashMap<>();
        // static persist -> repo, entity
        ownerPaths.put("/api/stores", new HashSet<>(Arrays.asList("GET", "POST", "PUT","PATCH", "DELETE")));
        ownerPaths.put("/api/menus", new HashSet<>(Arrays.asList("GET", "POST", "PUT","PATCH", "DELETE")));
        ownerPaths.put("/api/reviews", new HashSet<>(Arrays.asList("GET", "POST","PUT")));

        Map<String, Set<String>> userPaths = new HashMap<>();
        userPaths.put("/api/stores", new HashSet<>(List.of("GET")));
        userPaths.put("/api/reviews", new HashSet<>(List.of("GET","POST","PUT")));
        if (ownerPaths.containsKey(path)) {
            Set<String> allowedMethods = ownerPaths.get(path);
            if (!allowedMethods.contains(method) && !isOwner) {
                throw new CustomException(ErrorCode.FORBIDDEN);
            }
        }
        if (userPaths.containsKey(path)) {
            Set<String> allowedMethods = userPaths.get(path);
            if (!allowedMethods.contains(method) && !isUser) {
                throw new CustomException(ErrorCode.FORBIDDEN);
            }
        }


        filterChain.doFilter(request,response);
    }
}
