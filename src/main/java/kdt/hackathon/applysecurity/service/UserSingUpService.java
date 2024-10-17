package kdt.hackathon.applysecurity.service;

import kdt.hackathon.applysecurity.controller.dto.AddUserRequest;
import kdt.hackathon.applysecurity.entity.Role;
import kdt.hackathon.applysecurity.entity.User;
import kdt.hackathon.applysecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserSingUpService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        byte[] profileImageBytes = null;
        String profileUrl = null;

        if (!dto.getProfileImage().isEmpty()) {
            try {
                profileImageBytes = dto.getProfileImage().getBytes();
                String fileName = UUID.randomUUID() + "_" + dto.getProfileImage().getOriginalFilename();
                profileUrl = fileName;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to process the profile image", e);
            }
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .profileImage(profileImageBytes)
                .profileUrl(profileUrl)
                .role(Role.ROLE_USER)
                .build();

        return userRepository.save(user).getId();
    }
}




