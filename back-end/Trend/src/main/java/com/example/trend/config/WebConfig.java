package com.example.trend.config;

import com.example.trend.user.jwt.JwtInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
@EnableTransactionManagement
@EnableScheduling
public class WebConfig implements WebMvcConfigurer {
    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") // 모든 경로에 대해 인터셉터 적용
                // 제외할 경로 여기에 추가
                .excludePathPatterns(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/configuration/**",
                        "/api/user/duplicate-check/**",
                        "/api/item/rent/list",
                        "/api/item/rent/search",
                        "/api/item/rent/lessor/**",
                        "/api/course/detail/**"
                );
    }

    // Swagger UI가 필요로 하는 정적 파일(CSS, JS 등)을 제대로 제공하기 위해 리소스 핸들러를 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Swagger UI 리소스 핸들러 설정
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/")
                .resourceChain(false);
    }
}
