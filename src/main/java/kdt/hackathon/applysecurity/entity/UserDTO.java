package kdt.hackathon.applysecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private Role role;
}
// 엔티티가 가지는 책임이 데이터베이스와의 상호작용과 보안 관련 역할을 지니지 않도록
//  DTO 를 만들어서 사용
