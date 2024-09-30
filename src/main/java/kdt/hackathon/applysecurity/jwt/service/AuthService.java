package kdt.hackathon.applysecurity.jwt.service;

import kdt.hackathon.applysecurity.entity.User;
import kdt.hackathon.applysecurity.entity.UserDTO;
import kdt.hackathon.applysecurity.jsonLogin.controller.dto.LoginRequest;
import kdt.hackathon.applysecurity.jwt.config.JwtUtil;
import kdt.hackathon.applysecurity.jwt.controller.dto.JwtInfoDto;
import kdt.hackathon.applysecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public JwtInfoDto login(LoginRequest loginRequestDto) {
        String email = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("로그인 실패: 이메일 '{}'에 대한 비밀번호가 일치하지 않습니다.", email);

            throw new BadCredentialsException("Not Matches password");
        }

        UserDTO userDTO = user.toUserInfoDto();

        logger.info("로그인 성공: 이메일 '{}'에 대한 토큰이 생성되었습니다.", email);

        return jwtUtil.createToken(userDTO);
    }
}