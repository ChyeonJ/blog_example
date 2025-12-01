package chyeonj.blogexample.controller;

import chyeonj.blogexample.DTO.LoginRequest;
import chyeonj.blogexample.DTO.LoginResponse;
import chyeonj.blogexample.Service.UserService;
import chyeonj.blogexample.blogexam.domain.RefreshToken;
import chyeonj.blogexample.blogexam.domain.User;
import chyeonj.blogexample.config.jwt.TokenProvider;
import chyeonj.blogexample.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        //이메일 조회 + 비밀번호 체크
        User user = userService.findByEmail(request.getEmail());

        if (!userService.checkPassword(user, request.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        //Access / Refresh 토큰 생성
        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));
        String refreshToken = tokenProvider.generateToken(user, Duration.ofDays(7));

        //RefreshToken DB 저장 or 업데이트
        refreshTokenRepository.findByUserId(user.getId())
                .map(token -> token.update(refreshToken))
                .orElseGet(() -> refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken)));

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }
}
