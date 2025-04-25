package com.example.delivery.config.aop;


import com.example.delivery.config.annotation.AdminOnlyLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect // 이 클래스가 aop 기능을 담고 있다는 것을 알려주는 어노테이션
@Component
@RequiredArgsConstructor
public class AdminLogAspect {

    private final HttpServletRequest request;
    private final ObjectMapper objectMapper;

    @Around("@annotation(adminOnlyLog)") // 메서드 실행 전,후를 감싸서 AOP 동작을 하겠다는 의미.
    public Object logAdminApi(ProceedingJoinPoint joinPoint, AdminOnlyLog adminOnlyLog) throws Throwable {

        // 유저 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = (String) authentication.getPrincipal();
        Long userId = Long.parseLong(id);

        // 요청한 URL 가져오기
        String requestUrl = request.getRequestURI();

        // 요청 시각
        LocalDateTime requestTime = LocalDateTime.now();

        // 현재 실행 중인 메서드에 전달된 인자들을 배열로 반환
        Object[] args = joinPoint.getArgs();
        // JSON 형식의 문자열로 저장하기 위한 변수
        String requestBodyJson = "";

        Long storeId = null;
        Long orderId = null;

        // 요청 바디로 들어온 DTO를 JSON으로 직렬화
        for (Object arg : args) {
            if (arg != null && !(arg instanceof HttpServletRequest)) {
                requestBodyJson = objectMapper.writeValueAsString(arg);

                // 리플랙션 사용
                // 리플렉션을 사용해서 storeId, orderId가 있는지 확인, 필드가 없으면 무시하고 넘어감
                try {
                    var storeIdField = arg.getClass().getDeclaredField("storeId");
                    storeIdField.setAccessible(true);
                    storeId = (Long) storeIdField.get(arg);
                } catch (NoSuchFieldException ignored) {}

                try {
                    var orderIdField = arg.getClass().getDeclaredField("orderId");
                    orderIdField.setAccessible(true);
                    orderId = (Long) orderIdField.get(arg);
                } catch (NoSuchFieldException ignored) {}

                break; // 첫 번째 body만 기록
            }
        }

        // 메서드 실행
        Object result = joinPoint.proceed();

        // 	result가 ResponseEntity 객체인 경우, 응답 본문 (body)만 가져와서 JSON으로 직렬화. 그렇지 않으면 result 자체를 JSON으로 직렬화.
        String responseJson = objectMapper.writeValueAsString(result instanceof ResponseEntity ?
                ((ResponseEntity<?>) result).getBody() : result);

        // 로깅
        log.info("[ADMIN API LOG] userId={}, time={}, url={}, storeId={}, orderId={}, requestBody={}, responseBody={}",
                userId, requestTime, requestUrl, storeId, orderId, requestBodyJson, responseJson);
        //      누가,     언제,         어디에,                        무엇을 보냈고(요청),  무엇을 받았느지(응답)

        return result;
    }

}
