package com.womentech.server.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.womentech.server.exception.Code;
import com.womentech.server.exception.GeneralException;
import com.womentech.server.service.UserDetailsServiceImpl;
import com.womentech.server.util.JwtUtil;
import com.womentech.server.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Authorization : {}", authorization);

        if (request.getRequestURI().equals("/") || request.getRequestURI().equals("/user/login") || request.getRequestURI().equals("/user/join")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Token이 없을 경우 block
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new GeneralException(Code.UNAUTHORIZED, "Wrong Authorization");
            }

            // Token 꺼내기
            String token = authorization.split(" ")[1];

            // 로그아웃 되었는지 확인
            if (redisUtil.hasKeyBlackList(token)) {
                throw new GeneralException(Code.UNAUTHORIZED, "Logged out token detected");
            }

            // Token Expired 되었는지 확인
            if (jwtUtil.isTokenExpired(token)) {
                throw new GeneralException(Code.UNAUTHORIZED, "Token has expired");
            }

            // username Token에서 꺼내기
            String username = jwtUtil.getUsername(token);
            log.info("Username : {}", username);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 권한 부여
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // Detail 추가
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (GeneralException ex) {
            response.setStatus(ex.getErrorCode().getCode()); // 예외 코드의 HTTP 상태 코드를 사용
            response.setContentType("application/json"); // JSON 응답 타입으로 설정

            // 에러 응답 데이터 생성
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("code", ex.getErrorCode().getCode());
            errorResponse.put("message", ex.getErrorCode().getMessage() + " - " + ex.getMessage());

            // JSON 변환 후 응답 본문에 작성
            ObjectMapper objectMapper = new ObjectMapper();
            String errorResponseJson = objectMapper.writeValueAsString(errorResponse);

            response.getWriter().write(errorResponseJson);
            response.getWriter().flush();
        }
    }
}
