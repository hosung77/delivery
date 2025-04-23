package com.example.delivery.config;

import com.example.delivery.config.error.ErrorCode;
import com.example.delivery.config.error.ErrorResponseDTO;
import com.example.delivery.config.filter.JwtAuthenticationFilter;
import com.example.delivery.repository.auth.RefreshTokenRepository;
import com.example.delivery.service.auth.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 일반적인 헤더 인증에서는 CSRF 옵션을 킬 필요가 없다.
        // TODO 그런데 쿠키에 토큰을 저장하는 방식을 선택해서 CSRF 방어 옵션을 켜야 한다.
        http.csrf(AbstractHttpConfigurer::disable);
        // 프론트엔드 CORS 설정
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        // 토큰기반 인증을 사용하므로 별도의 세션 관리를 하지 않는다.
        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        // API 서버이므로 폼로그인 옵션을 끈다.
        http.formLogin(AbstractHttpConfigurer::disable);
        // 토큰이 있으면 인증정보를 만드는 필터를 UsernamePasswordFilter 앞에 필터체인에 등록한다.
        http.addFilterBefore(new JwtAuthenticationFilter(tokenService, objectMapper, refreshTokenRepository),
                UsernamePasswordAuthenticationFilter.class);
        // Auth permitAll Endpoints
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/token").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/admin/test").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
        );
        // 예외처리는 하단 부에 적용
        http.exceptionHandling((handling) -> handling.authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler()));

        return http.build();

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // Security에서 발생하는 401 Exception Handler
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ResponseEntity<ErrorResponseDTO> error = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponseDTO.from(ErrorCode.AUTH_UNAUTHORIZED, request.getRequestURI()));

            response.getWriter().write(objectMapper.writeValueAsString(error));
        };
    }

    @Bean // Security에서 발생하는 403 Exception Handler
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ResponseEntity<ErrorResponseDTO> error = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponseDTO.from(ErrorCode.FORBIDDEN, request.getRequestURI()));

            response.getWriter().write(objectMapper.writeValueAsString(error));
        };
    }



}
