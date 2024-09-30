//package kdt.hackathon.applysecurity.jwt.controller;
//
//import kdt.hackathon.applysecurity.jsonLogin.controller.dto.LoginRequest;
//import kdt.hackathon.applysecurity.jwt.controller.dto.CreateTokenRequest;
//import kdt.hackathon.applysecurity.jwt.controller.dto.CreateTokenResponse;
//import kdt.hackathon.applysecurity.jwt.service.TokenService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RestController
//public class TokenApiController {
//    private final TokenService tokenService;
//
//    @PostMapping("/api/token")
//    public ResponseEntity<CreateTokenResponse> createToken(@RequestBody CreateTokenRequest request) {
//        String newAccessToken = tokenService.createNewToken(request.getRefreshToken());
//
//        return  ResponseEntity.status(HttpStatus.CREATED).body(new CreateTokenResponse(newAccessToken));
//
//    }
//
////    @PostMapping("/api/login")
////    public ResponseEntity<LoginRequest> checkToken(@RequestBody LoginRequest request) {
////        String accessToken = tokenService.createNewToken(request);
////    }
////
////
////        @PostMapping("/login")
////        public ResponseEntity<JwtInfoDto> login(@RequestBody LoginRequestDto loginRequestDto) {
////            JwtInfoDto jwtInfoDto = authService.login(loginRequestDto);
////            return ResponseEntity.ok(jwtInfoDto);
////        }
////    }
//
//}
