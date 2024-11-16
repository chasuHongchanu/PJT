package com.example.trend.user.jwt;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 컨트롤러 핸들러가 호출되기 전에 JWT 토큰을 검증
     *
     * @param request  HTTP 요청
     * @param response HTTP 응답
     * @param handler  핸들러
     * @return true: 핸들러 실행, false: 핸들러 실행 중단
     * @throws Exception 예외 발생 시
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 헤더에서 Authorization 추출
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 액세스 토큰이 없거나 형식이 올바르지 않음
            log.error("토큰이 없거나 형식이 올바르지 않습니다.");
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        String accessToken = authHeader.substring(7); // "Bearer " 이후의 토큰 부분

        try {
            // 액세스 토큰 검증
            Claims claims = jwtUtil.validateToken(accessToken);

            // 사용자 정보를 요청 속성에 저장하여 컨트롤러에서 사용할 수 있게 함
            request.setAttribute("userId", claims.getSubject());
            request.setAttribute("userNickname", claims.get("nickname"));

            return true;
        } catch (JwtException e) {
            // 액세스 토큰이 유효하지 않거나 만료됨
            log.error("토큰이 유효하지 않거나 만료되었습니다.");
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

}