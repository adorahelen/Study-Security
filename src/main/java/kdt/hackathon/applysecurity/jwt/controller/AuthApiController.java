package kdt.hackathon.applysecurity.jwt.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import kdt.hackathon.applysecurity.jsonLogin.controller.dto.LoginRequest;
import kdt.hackathon.applysecurity.jwt.controller.dto.JwtInfoDto;
import kdt.hackathon.applysecurity.jwt.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthApiController.class);



    @PostMapping("/api/login")
    public String login(@RequestBody LoginRequest loginRequestDto, HttpServletResponse response) {
        JwtInfoDto jwtInfoDto = authService.login(loginRequestDto);

        Cookie cookie = new Cookie("jwt", "Bearer+" + jwtInfoDto.getAccessToken());
        cookie.setPath("/"); // 쿠키의 경로를 설정하여 "/" 이하의 모든 경로에서 접근 가능하도록 합니다.
        cookie.setSecure(true); // 쿠키가 HTTPS 연결을 통해서만 전송되도록 설정합니다.
        cookie.setHttpOnly(true); // 쿠키에 대한 JavaScript 접근을 방지하여 보안을 강화합니다.
        cookie.setMaxAge(60 * 25); // 쿠키의 유효 시간을 25분(1500초)으로 설정합니다.

        response.addCookie(cookie); // 생성한 쿠키를 응답에 추가하여 클라이언트에 전송합니다.

        logger.info("쿠키 생성 완료: 쿠키 이름 '{}', 만료 시간 {}", cookie.getName(), cookie.getMaxAge());
        return "/service"; // 로그인 성공 후 사용자 서비스 페이지로 이동
    }



}