package kdt.hackathon.applysecurity.jwt.service;

import kdt.hackathon.applysecurity.entity.User;
import kdt.hackathon.applysecurity.jwt.JwtTokenProvider;
import kdt.hackathon.applysecurity.service.UserFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserFindService userFindService;

    public String createNewToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        Long userId = refreshTokenService.findByToken(refreshToken).getUserId();
        User user = userFindService.findById(userId);
        return jwtTokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
