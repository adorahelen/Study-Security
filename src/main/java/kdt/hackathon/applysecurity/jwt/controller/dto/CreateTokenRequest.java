package kdt.hackathon.applysecurity.jwt.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTokenRequest {
    private String refreshToken;
}
