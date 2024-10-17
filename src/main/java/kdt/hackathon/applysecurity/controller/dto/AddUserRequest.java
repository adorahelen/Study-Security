package kdt.hackathon.applysecurity.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class AddUserRequest { // 뷰 단에서, 회원가입시 보낼 정보들
    private String email;
    private String password;
    private String nickname;
    private MultipartFile profileImage;


}
