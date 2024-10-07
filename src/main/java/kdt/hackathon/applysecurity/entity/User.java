package kdt.hackathon.applysecurity.entity;

import jakarta.persistence.*;
import lombok.*;


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


    @Builder
    public User( String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;

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
