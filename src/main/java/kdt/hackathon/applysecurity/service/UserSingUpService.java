package kdt.hackathon.applysecurity.service;

import kdt.hackathon.applysecurity.controller.dto.WebSingUpRequest;
import kdt.hackathon.applysecurity.entity.Role;
import kdt.hackathon.applysecurity.entity.User;
import kdt.hackathon.applysecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserSingUpService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(WebSingUpRequest singUpData) {


        Boolean isExist = userRepository.existsByEmail(singUpData.getEmail());

        if (isExist) {
            return null;
        }

        else {
            User user = User.builder()
                    .email(singUpData.getEmail())
                    .password(bCryptPasswordEncoder.encode(singUpData.getPassword()))
                    .role(Role.ROLE_USER)
                    .build();

            return userRepository.save(user).getId();
        }

    }
}




