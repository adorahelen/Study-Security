package kdt.hackathon.applysecurity.config;

import kdt.hackathon.applysecurity.jwt.handler.CustomAccessDeniedHandler;
import kdt.hackathon.applysecurity.jwt.handler.CustomAuthenticationEntryPoint;
import kdt.hackathon.applysecurity.jwt.config.JwtAuthenticationFilter2;
import kdt.hackathon.applysecurity.jwt.config.JwtUtil;
import kdt.hackathon.applysecurity.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;




@Configuration
@RequiredArgsConstructor
public class SecurityConfigJwt {

    private final CustomUserDetailsService CustomUserDetailsService;
    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public WebSecurityCustomizer configure() { //스프링 시큐리티 기능 비활성화
        return (web) -> web.ignoring()
                .requestMatchers("/img/**", "/css/**", "/js/**"); //정적 자원에 대한 경로를 보안 검사에서 제외
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //CSRF, CORS

        http.csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화 (API에서 주로 사용)
                .httpBasic(httpBasic -> httpBasic.disable())  // 기본 HTTP 인증 비활성화
                .formLogin(formLogin -> formLogin.disable())  // 기본 폼 로그인 비활성화
                .logout(logout -> logout.disable());  // 기본 로그아웃 비활성화


        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        // 세션 무상태(STATELESS)가 맞지만, 그러면 사용자 정보를 매번 프론트에서 받아와야 함
        // => 처음 한번은 무조건 받아와야 하지만, 한번 받아서 인증 시키면 그 이후로는 시큐리티 컨텍스트 홀더 혹은 프린시펄 혹은 어센틱케이션에서 가능


        // JwtAuthFilter를 filterChain에 추가 (모든 요청 가로채고, 인증 객체 생성 후 시큐리티 홀더에 저장 여기서)
        // 일반 스프링 시큐리티의 경우 매니저&프로바이더 등이 필요하지만 jwt 로 커스텀 하는 경우 필터에서 바로 때려밖음
        http.addFilterBefore(jwtAuthenticationFilter2(), UsernamePasswordAuthenticationFilter.class);


        http.authorizeRequests()
                .requestMatchers("/api/token", "/api/upload/**", "/api/login", "/ws/**").permitAll() // 인증 필요 없음
                .requestMatchers("/api/**").authenticated()  // API 요청은 인증이 필요
                //  .requestMatchers("/mypage").authenticated() // Only authenticated users can access
                .anyRequest().permitAll();  // 그 외 모든 요청은 인증 필요 없음

        // 핸들링 정의 및 구성
//        http.exceptionHandling((exceptionHandling) -> exceptionHandling
//                .authenticationEntryPoint(authenticationEntryPoint)
//                .accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }

    //JWT 토큰을 확인하고 인증하는 커스텀 필터
    @Bean
    public JwtAuthenticationFilter2 jwtAuthenticationFilter2() {
        return new JwtAuthenticationFilter2(jwtUtil, CustomUserDetailsService);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

