package com.womentech.server.service;

import com.womentech.server.configuration.JwtProvider;
import com.womentech.server.domain.User;
import com.womentech.server.domain.dto.response.JwtResponse;
import com.womentech.server.exception.GeneralException;
import com.womentech.server.exception.Code;
import com.womentech.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager manager;
    private final JwtProvider jwtProvider;

    @Value("${jwt.secret}")
    private String secretKey;

    @Transactional
    public void join(String username, String name, String password) {
        // identifier 중복 check
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new GeneralException(Code.BAD_REQUEST, "User ID already exists");
                });

        // 저장
        User user = User.builder()
                .username(username)
                .name(name)
                .password(encoder.encode(password))
                .build();
        userRepository.save(user);
    }

    @Transactional
    public JwtResponse login(String username, String password) {
        // 실패 - identifier 없음
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(Code.NOT_FOUND, "User ID doesn't exist"));

        // 실패 - password 틀림
        if (!encoder.matches(password, user.getPassword())) {
            throw new GeneralException(Code.BAD_REQUEST, "Invalid password");
        }

        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        return new JwtResponse(
                jwtProvider.createAccessJwt(authentication),
                jwtProvider.createRefreshJwt(authentication)
        );
    }

    @Transactional
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    @Transactional
    public void editName(String username, String name) {
        User user = userRepository.findByUsername(username).orElse(null);
        user.setName(name);
    }

    @Transactional
    public void editPassword(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);
        user.setPassword(encoder.encode(password));
    }
}
