package kdt.hackathon.applysecurity.jwt.controller.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
