package com.example.delivery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // CreatedAt, UpdatedAt 기능 추가
@Configuration
public class JpaConfig {
}