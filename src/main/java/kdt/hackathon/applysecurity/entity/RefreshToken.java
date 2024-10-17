package kdt.hackathon.applysecurity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
//@Table(name = "refresh_token")
@Getter
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name="user_id",nullable = false)
    private Long userId;

    @Column(name = "refresh_token",nullable = false, length = 1000)
    private String refreshToken;

    @Column(name = "email", length = 255)
    private String email;

    public RefreshToken(Long userId, String refreshToken, String email) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.email = email;
    }

    public RefreshToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }
}
