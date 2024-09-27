package kdt.hackathon.applysecurity.entity;

import jakarta.persistence.*;
//import kdt.hackathon.applysecurity.dto.UserInfoDetailDTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@Table(name = "user", schema = "itclips")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false, length = 511)
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "job", length = 50)
    private String job;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "refresh_token", length = 511)
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User(User build) {
    }


    public User update(String profileImage) {
        this.profileImage = profileImage;
        this.updatedAt = LocalDateTime.now();
        return this;
    }


    @Builder
    public User(Long id, String email, String password, String nickname, String profileImage,
                LocalDateTime createdAt, LocalDateTime updatedAt, LocalDate birth, String job, Boolean gender,
                String refreshToken, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.birth = birth;
        this.job = job;
        this.gender = gender;
        this.refreshToken = refreshToken;
        this.role = role;

    }

//    public UserInfoDetailDTO convertToUserInfoDetailDTO(String imageUrl) {
//        return UserInfoDetailDTO.builder()
//                .id(this.id)
//                .email(this.email)
//                .nickname(this.nickname)
//                .image(imageUrl)
//                .birth(this.birth)
//                .job(this.job)
//                .gender(this.gender)
//                .build();
//    }

    public void setImageToS3FileName(String fileName) {
        this.profileImage=fileName;
    }
}
