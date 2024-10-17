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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;


@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter2 extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    private final static String TOKEN_PREFIX = "Bearer "; //JWT 토큰의 접두사, 일반적으로 "Bearer "로 사용
    private final static String HEADER_AUTHORIZATION = "Authorization"; //Authorization 헤더의 키 값, 즉 클라이언트가 인증 토큰을 보낼 때 사용하는 HTTP 헤더 이름.



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


      //  String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION); // 요청 헤더에서 Authorization 헤더의 값을 가져온다. 여기에는 토큰이 포함됨
      //  String token = authorizationHeader.substring(7); // 위에 베어가 7자리
        String token = getAccessToken(authorizationHeader); //헤더에서 Bearer 접두사를 제거한 실제 JWT 토큰을 추출
        log.info("http 리퀘스트로 날아온 헤더(토큰 찍혀야함) : " + authorizationHeader); // 헤더 받아서 찍기

        // 토큰이 잘 담겨 있는 경우

        if(jwtUtil.validateToken(token)) {
            Authentication auth = jwtUtil.getAuthentication(token); // 유효한 토큰인 경우, 토큰을 사용해 인증 정보를 가져온다.
            SecurityContextHolder.getContext().setAuthentication(auth); //인증 정보를 Spring Security의 SecurityContext에 저장한다. 이렇게 설정된 인증 정보는 이후 요청이 처리될 때 참고된다.
        }

        filterChain.doFilter(request, response); //필터 체인 내의 다음 필터로 요청을 전달함
    }

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

    private String getAccessToken(String authorizationHeader) {
        if(authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) { //헤더가 존재하고 Bearer로 시작하는지 확인
            return authorizationHeader.substring(TOKEN_PREFIX.length()); //Bearer 부분을 제거하고 순수한 토큰만 추출하여 반환
        }
        return null;
    }
}
