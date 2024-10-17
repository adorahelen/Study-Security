package kdt.hackathon.applysecurity.jwt.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import kdt.hackathon.applysecurity.jwt.controller.dto.LoginRequest;
import kdt.hackathon.applysecurity.jwt.controller.dto.JwtInfoDto;
import kdt.hackathon.applysecurity.jwt.service.JwtLoginService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class JwtLoginApiController {

    private final JwtLoginService jwtLoginService;
    private static final Logger logger = LoggerFactory.getLogger(JwtLoginApiController.class);

    @PostMapping("/api/login")
    public String login(@RequestBody LoginRequest loginRequestDto, HttpServletResponse response) {
        JwtInfoDto jwtInfoDto = jwtLoginService.login(loginRequestDto);

        // (Http 응답 안에) Authorization 헤더에 AccessToken 추가
        response.setHeader("Authorization", "Bearer " + jwtInfoDto.getAccessToken());

        // 쿠키 설정 의미 x ?
        Cookie cookie = new Cookie("jwt", "Bearer+" + jwtInfoDto.getAccessToken());
        cookie.setPath("/"); // 쿠키의 경로를 설정하여 "/" 이하의 모든 경로에서 접근 가능하도록
        cookie.setSecure(true); // 쿠키가 HTTPS 연결을 통해서만 전송되도록 설정
        cookie.setHttpOnly(true); // 쿠키에 대한 JavaScript 접근을 방지하여 보안을 강화
        cookie.setMaxAge(60 * 25); // 쿠키의 유효 시간을 25분(1500초)으로 설정
        response.addCookie(cookie); // 생성한 쿠키를 응답에 추가하여 클라이언트에 전송 -> 위 http 응답과 중복?


        logger.info("Authorization 헤더에 AccessToken이 추가되었습니다: {}", jwtInfoDto.getAccessToken());
        logger.info("쿠키 생성 완료: 쿠키 이름 '{}', 만료 시간 {}", cookie.getName(), cookie.getMaxAge());
        return "/service"; // 로그인 성공 후 사용자 서비스 페이지로 이동
    }
    // 뷰페이지를 이동시키는건, 성공핸들러에서 처리시키고
    // 여기서는  return ResponseEntity.ok(jwtInfoDto); 등을 반환시키는게 맞지 않을까?

//    @PostMapping("/login")
//    public ResponseEntity<JwtInfoDto> login(@RequestBody LoginRequestDto loginRequestDto) {
//        JwtInfoDto jwtInfoDto = authService.login(loginRequestDto);
//        return ResponseEntity.ok(jwtInfoDto);
//    }
}