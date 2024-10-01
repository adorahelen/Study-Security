package kdt.hackathon.applysecurity.jwt.controller;

import jakarta.servlet.http.HttpServletRequest;
import kdt.hackathon.applysecurity.jwt.controller.dto.AccessTokenDto;
import kdt.hackathon.applysecurity.jwt.service.ReissueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jwt")
public class ReissueController {

    private final ReissueService reissueService;

    @PostMapping("/reissue")
    public ResponseEntity<AccessTokenDto> reissueAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String refreshToken = header.substring(7);

        AccessTokenDto accessTokenDto = reissueService.reissueAccessTokenByRefreshToken(refreshToken);

        return ResponseEntity.ok(accessTokenDto);
    }
}