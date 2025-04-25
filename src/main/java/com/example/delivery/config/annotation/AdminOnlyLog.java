package com.example.delivery.config.annotation;


import java.lang.annotation.*;

@Target(ElementType.METHOD) // 메서드에만 적용
@Retention(RetentionPolicy.RUNTIME) // 런타임에 유지
@Documented
public @interface AdminOnlyLog {
}
