package kdt.hackathon.applysecurity.jwt.config;

import lombok.RequiredArgsConstructor;

import java.security.Principal;


// 시큐리티가 사용하는 SecurityContext + 커스텀 필터의 통합
// => @PreAuthorize (hasRole : 표현식 가능하게함)
// 토큰을 이용해서 Security Context 안에 인증되었다는 정보를 추가하는 과정 중, 가장 중요한 내용물 "Principal"
@RequiredArgsConstructor
public class JwtPrincipal implements Principal {

    private final String username;


    @Override
    public String getName() {
        return username;
    }

}
