package kdt.hackathon.applysecurity.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // 흐름
    // 1. 요청 -> [토큰 필터] if 유효한 토큰 -> 2. 인증 정보 저장[컨텍스트 홀더] -> 3. 로직 실행(서비스 단)
    //       응답  <-
    //                * else 유효하지 않은 토큰 -> 별도의 로직 실행(서비스 단)

    private final JwtTokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";


    // 요청(헤데)에서 키가 Authorization 인 필드의 값을 가져온 다음 토큰의 접두사 Bearer 를 제외한 값을 얻는다.
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authrizationHeader = request.getHeader(HEADER_AUTHORIZATION); // 요청 헤더의 값 조회
        String token = getAccessToken(authrizationHeader);

        // 가져온 토큰이 유효한지 확인하고, 유효한 때는 인증 정보 설정
        if (tokenProvider.validateToken(token)) {
            // 토큰이 유효한 경우 :
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 토큰에서 인증 정보를 빼서 -> 시큐리티 컨텍스트에 인증 정보를 설정(저장)
        }

        filterChain.doFilter(request, response); // 필터 체인, 서블릿 필터 체인
    }

    private String getAccessToken(String authrizationHeader) {
        if (authrizationHeader != null && authrizationHeader.startsWith(TOKEN_PREFIX)) {
            return authrizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
