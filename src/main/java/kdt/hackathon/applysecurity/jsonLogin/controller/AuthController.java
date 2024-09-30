package kdt.hackathon.applysecurity.jsonLogin.controller;

import kdt.hackathon.applysecurity.jsonLogin.controller.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

//    @PostMapping("/api/login")
//    public String login(@RequestBody LoginRequest loginRequest) {
//        logger.info("Received login request: username={}, password={}", loginRequest.getUsername(), loginRequest.getPassword());
//
//        try {
//            UsernamePasswordAuthenticationToken authenticationToken =
//                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
//
//            logger.debug("Attempting to authenticate user: {}", loginRequest.getUsername());
//            Authentication authentication = authenticationManager.authenticate(authenticationToken);
//            SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 정보를 SecurityContext 에 저장
//
//            logger.info("Authentication successful for user: {}", loginRequest.getUsername());
//
//            return "Login successful for user: " + loginRequest.getUsername();
//        } catch (AuthenticationException e) {
//            logger.error("Login failed for user {}: {}", loginRequest.getUsername(), e.getMessage());
//            return "Login failed: " + e.getMessage();
//        }
//    }

@PostMapping("/api/login")
public String login(@RequestBody LoginRequest loginRequest) {
    logger.info("Login request received for username: {}", loginRequest.getUsername());

    // 로그인 처리는 CustomAuthenticationFilter에서 수행되기 때문에 여기서 직접적인 처리 없음
    return "Login request submitted for username: " + loginRequest.getUsername();
}


}