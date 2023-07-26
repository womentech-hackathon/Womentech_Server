package com.womentech.server.service;

import com.womentech.server.domain.User;
import com.womentech.server.exception.AppException;
import com.womentech.server.exception.ErrorCode;
import com.womentech.server.repository.UserRepository;
import com.womentech.server.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret}")
    private String secretKey;

    private Long expiredMs = 1000 * 60 * 60l;   // 1 hour

    public void join(String identifier, String name, String password) {
        // identifier 중복 check
        userRepository.findByIdentifier(identifier)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.IDENTIFIER_DUPLICATED, "이미 존재하는 아이디입니다.");
                });

        // 저장
        User user = User.builder()
                .identifier(identifier)
                .name(name)
                .password(encoder.encode(password))
                .build();
        userRepository.save(user);
    }

    public String login(String identifier, String password) {
        // 실패 - identifier 없음
        User user = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new AppException(ErrorCode.IDENTIFIER_NOT_FOUND, "존재하지 않는 아이디입니다."));

        // 실패 - password 틀림
        if (!encoder.matches(password, user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다.");
        }

        String token = JwtUtil.createJwt(user.getId(), secretKey, expiredMs);

        // 성공 -> 토큰 발행
        return token;
    }
}
