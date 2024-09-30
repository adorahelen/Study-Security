//package kdt.hackathon.applysecurity.jwt.controller;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
////@RequestMapping("/api/v1/jwt")
//public class JwtController {
//
//    private final JwtService jwtService;
//
//    @PostMapping("/reissue")
//    public ResponseEntity<AccessTokenDto> reissueAccessToken(HttpServletRequest request) {
//        String header = request.getHeader("Authorization");
//        String refreshToken = header.substring(7);
//
//        AccessTokenDto accessTokenDto = jwtService.reissueAccessTokenByRefreshToken(refreshToken);
//
//        return ResponseEntity.ok(accessTokenDto);
//    }
//}