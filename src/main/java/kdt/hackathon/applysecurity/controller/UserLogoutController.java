package kdt.hackathon.applysecurity.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserLogoutController {
//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request,
//                         HttpServletResponse response) {
//        new SecurityContextServerLogoutHandler().logout(request, response,
//                SecurityContextHolder.getContext().getAuthentication());
//        return "redirect:/login";
//
//    }
    // 1. 컨피그에서 로그아웃 설정 해놈, 2. 세션 방식 아니라서 로그아웃시 별도의 토큰 혹은 오쓰 쿠기 삭제 요청 필요?
}
