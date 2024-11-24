package com.example.trend.user.jwt;

import com.example.trend.config.SkipJwt;
import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(request.getMethod() + " : " + request.getRequestURI());
        log.info("Authorization:" + request.getHeader("Authorization"));

        // Preflight 요청 처리
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Max-Age", "3600");
            return true;
        }

        // @SkipJwt 어노테이션 처리
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            if (method.isAnnotationPresent(SkipJwt.class) ||
                    handlerMethod.getBeanType().isAnnotationPresent(SkipJwt.class)) {
                return true;
            }
        }

        // 토큰 검증
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!jwtUtil.hasAuthHeader(authHeader)) {
            log.error("토큰이 없거나 형식이 올바르지 않습니다.");
            throw new CustomException(ErrorCode.NOT_ACCESS_TOKEN);
        }

        try {
            String accessToken = authHeader.substring(7);
            Claims claims = jwtUtil.validateToken(accessToken);
            request.setAttribute("userId", claims.getSubject());
            request.setAttribute("userNickname", claims.get("nickname"));
            return true;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN, e);
        }
    }
}