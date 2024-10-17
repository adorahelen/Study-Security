package kdt.hackathon.applysecurity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Base64;


@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false, length = 511)
    private String password;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    //OAuth관련키 저장
    @Column(name="nickname",unique = true)
    private String nickname;

    @Lob
    @Column(name = "profile_image", columnDefinition = "LONGBLOB")
    private byte[] profileImage;  // 이미지 자체 저장

    @Column(name = "profile_url")
    private String profileUrl;  // 이미지 URL 저장

    @Builder
    public User(String email, String password, String nickname, byte[] profileImage, String profileUrl, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.profileUrl = profileUrl;  // URL 저장(이미지 구별, 호출)
        this.role = role;
    }

    // 프로필 이미지를 Base64 문자열로 변환
    public String getProfileImageAsBase64() {
        if (profileImage != null) {
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(profileImage);
        }
        return null;
    }

    // 프로필 이미지 변경 메소드 추가
    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    //사용자 이름 변경
    public User update(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public UserDTO toUserInfoDto() {
        return UserDTO.builder()
                .id(getId())
                .email(getEmail())
                .password(getPassword())
                .role(getRole())
                .build();
    }
}
