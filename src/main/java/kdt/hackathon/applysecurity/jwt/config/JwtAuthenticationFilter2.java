package kdt.hackathon.applysecurity.jwt.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kdt.hackathon.applysecurity.entity.Role;
import kdt.hackathon.applysecurity.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
// 이거 말고도 프린시펄도 필요한데, 프린시펄을 안 만들어줬으니 ,,,,
//import org.springframework.security.core.parameters.P;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter2 extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        if(request.getServletPath().startsWith("/api/login")) {
            return true;
        }
        return false;
        // 경로 지정 필요
    }

    // OncePerRequestFilter
    // 1. 슈드 낫 필터 & 2. 두 필터를 오버라이드 한다.
    // 이 중 1. 슈드 낫 필터는 jwt 필터가 동작하지 않아야 하는 경로를 지정하기 위해서 사용
    //      2. 두 필터는, 엑세스 토큰을 꺼내서 검증한다음 문제가 없는 경우 => @controller || 다른 필터


    // * 요청시에 엑세스 토큰이 어떻게 전달할 것인가 대해서는 많지만
    //      일단은 Authorization 헤더에 [인증 타입 + 토큰 값] 전달


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        log.info(" 제이슨 웹 토큰 필터 : 두 필터 ");
        log.info("requestURI : " + request.getRequestURI());

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorizationHeader : " + authorizationHeader); // 헤더 받아서 찍기

        // 엑세스 토큰이 담겨있지 않거나 || 베어로 시작하지 않는 경우
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            handleException(response, new Exception("엑세스 토큰을 찾을 수 없습니다."));

            return;
        }

        // 토큰이 잘 담겨 있는 경우
        String token = authorizationHeader.substring(7); // 위에 베어가 7자리

        try {
            jwtUtil.validateToken(token);

            log.info("검증한 토큰 token : " + token);

            // 프린시펄 설정을 위한 빌드업
            String username = customUserDetailsService.loadUserByUsername(token).getUsername();
            Role role = Role.ROLE_USER;
            // 인증 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            new JwtPrincipal(username),
                            null,
                             Arrays.asList(new SimpleGrantedAuthority(role.getAuthority()))
                );

            // 시쿠리티 컨텍스트 홀더에에 인증(토큰)객체 저장
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authenticationToken);
            System.out.println("왜 안되는 걸까? => 성공");
            System.out.println(SecurityContextHolder.getContext().getAuthentication());

            filterChain.doFilter(request, response);
        }catch (Exception e) {
            // 문제가 발생했다면
            handleException(response, e);
        }
    }
    // ===============================  =============== =============== =============== ===============

    private void handleException(HttpServletResponse response, Exception exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().println("{ \"error\": \"" + exception.getMessage() + "\" }");
    }
}
