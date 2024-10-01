package kdt.hackathon.applysecurity.jwt.service;

import kdt.hackathon.applysecurity.entity.User;
import kdt.hackathon.applysecurity.entity.UserDTO;
import kdt.hackathon.applysecurity.jwt.config.JwtUtil;
import kdt.hackathon.applysecurity.jwt.controller.dto.AccessTokenDto;
import kdt.hackathon.applysecurity.service.UserFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReissueService {


    private final UserFindService userFindService;
    private final JwtUtil jwtUtil;

    public AccessTokenDto reissueAccessTokenByRefreshToken(String refreshToken) {
        // 1. 리프레시 토큰에서 사용자 ID 추출
        Long userId = jwtUtil.getUserIdFromToken(refreshToken); // getUserIdFromToken 메소드 추가 필요

        // 2. 사용자 ID를 통해 사용자 정보를 조회
        User user = userFindService.findById(userId);
        UserDTO userDTO = user.toUserInfoDto();

        // 3. 엑세스 토큰 만료 시간 설정
        // Date accessTokenExpireTime = jwtUtil.createAccessTokenExpireTime();
        Date accessTokenExpireTime = new Date(System.currentTimeMillis() + jwtUtil.getAccessTokenExpireTime());

        // 4. 새 엑세스 토큰 생성
        String accessToken = jwtUtil.createAccessToken(userDTO, accessTokenExpireTime);

        // 5. 새 엑세스 토큰을 포함한 DTO 반환
        return AccessTokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .build();
    }
}