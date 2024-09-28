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
@Table(name = "user", schema = "itclips")
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


    @Builder
    public User( String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;

    }
}
