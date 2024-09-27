package kdt.hackathon.applysecurity.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "members")
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(value = { AuditingEntityListener.class })
public class Member {
    @Id
    private String id;
    private String mpw;
    private String mname;
    private String email;

    @CreatedDate
    private LocalDateTime joinDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    // 일반 회원, 관리자
    private String role;

    public void changePassword(String password) {
        this.mpw = password;
    }
    public void changeName(String name){
        this.mname = name;
    }
    public void changeEmail(String email){
        this.email = email;
    }
    public void changeRole(String role){
        this.role = role;
    }

}
