//package kdt.hackathon.applysecurity.jsonLogin.controller;
//
//import kdt.hackathon.applysecurity.jwt.controller.dto.LoginRequest;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//public class AuthController {
//
//    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
//
//@PostMapping("/api/login")
//public String login(@RequestBody LoginRequest loginRequest) {
//    logger.info("Login request received for username: {}", loginRequest.getUsername());
//
//    // 로그인 처리는 CustomAuthenticationFilter에서 수행되기 때문에 여기서 직접적인 처리 없음
//    return "Login request submitted for username: " + loginRequest.getUsername();
//}
//
//
//}