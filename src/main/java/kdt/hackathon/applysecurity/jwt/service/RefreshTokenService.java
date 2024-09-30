//package kdt.hackathon.applysecurity.jwt.service;
//
//import kdt.hackathon.applysecurity.jwt.entity.RefreshToken;
//import kdt.hackathon.applysecurity.jwt.repository.RefreshTokenRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Service
//public class RefreshTokenService {
//    private final RefreshTokenRepository refreshTokenRepository;
//
//    public RefreshToken findByToken(String refreshToken) {
//        return refreshTokenRepository.findByRefreshToken(refreshToken)
//                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
//    }
//}
