package com.womentech.server.service;

import com.womentech.server.domain.User;
import com.womentech.server.exception.AppException;
import com.womentech.server.exception.ErrorCode;
import com.womentech.server.repository.UserRepository;
import com.womentech.server.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String key;
    private Long expireTimeMs = 1000 * 60 * 60l;

    public void join(String identifier, String name, String password) {
        // identifier 중복 check
        userRepository.findByIdentifier(identifier)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.IDENTIFIER_DUPLICATED, identifier + "는 이미 있습니다.");
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
                .orElseThrow(() -> new AppException(ErrorCode.IDENTIFIER_NOT_FOUND, identifier + "이 없습니다."));

        // 실패 - password 틀림
        if (!encoder.matches(password, user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다.");
        }

        String token = JwtTokenUtil.createToken(user.getIdentifier(), key, expireTimeMs);

        // 성공 -> 토큰 발행
        return token;
    }
}