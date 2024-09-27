package kdt.hackathon.applysecurity.service;

import kdt.hackathon.applysecurity.controller.dto.WebSingUpRequest;
import kdt.hackathon.applysecurity.entity.User;
import kdt.hackathon.applysecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserSingUpService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(WebSingUpRequest singUpData) {
        User user = new User(User.builder()
                .email(singUpData.getEmail())
                .password(bCryptPasswordEncoder.encode(singUpData.getPassword()))
                .build());

        return userRepository.save(user).getId();

    }
}
