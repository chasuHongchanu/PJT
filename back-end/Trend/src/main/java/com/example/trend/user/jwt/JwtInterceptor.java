package com.example.trend.user.jwt;

import com.example.trend.config.SkipJwt;
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
        // @SkitJwt 설정한 url메서드에 대해 인터셉터 로직 건너뛰기
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            // 메서드나 클래스에 @SkipJwt 애노테이션이 있는지 확인
            if (method.isAnnotationPresent(SkipJwt.class) ||
                    handlerMethod.getBeanType().isAnnotationPresent(SkipJwt.class)) {
                return true;
            }
        }

        // 헤더에서 Authorization 추출
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!jwtUtil.hasAuthHeader(authHeader)) {
            // 액세스 토큰이 없거나 형식이 올바르지 않음
            log.error("토큰이 없거나 형식이 올바르지 않습니다.");
            throw new CustomException(ErrorCode.NOT_ACCESS_TOKEN);
        }

        String accessToken = authHeader.substring(7); // "Bearer " 이후의 토큰 부분

        try {
            // 액세스 토큰 검증
            Claims claims = jwtUtil.validateToken(accessToken);

            // 사용자 정보를 요청 속성에 저장하여 컨트롤러에서 사용할 수 있게 함
            request.setAttribute("userId", claims.getSubject());
            request.setAttribute("userNickname", claims.get("nickname"));

            return true;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
    }

}
