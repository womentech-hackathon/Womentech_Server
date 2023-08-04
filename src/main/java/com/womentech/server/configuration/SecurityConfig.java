package com.womentech.server.configuration;

import com.womentech.server.service.UserDetailsServiceImpl;
import com.womentech.server.util.JwtUtil;
import com.womentech.server.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeHttpRequests()
                .requestMatchers("/", "/user/login", "/user/join").permitAll()
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()   // Swagger UI 예외 추가
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용하지 않음
                .and()
                .addFilterBefore(new JwtFilter(jwtUtil, redisUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class) // JWT 적용
                .build();
    }
}
