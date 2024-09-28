package kdt.hackathon.applysecurity.config;

import kdt.hackathon.applysecurity.formLogin.CustomAuthFailureHandler;
import kdt.hackathon.applysecurity.formLogin.CustomAuthSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthSuccessHandler authSuccessHandler;
    private final CustomAuthFailureHandler authFailureHandler;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**", "/", "/login", "/signup","/user",
            "/api/v1/auth/**"
    }; // 없어도 ㄱㅊ -> 프론트에서 쏘는 api 도 등록 안해놓으면 시큐리티가 전부 거름

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //CSRF, CORS, BasicHttp 비활성화
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());
        http.httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable());

        //세션 관리 구성
        http
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        //ForLogin, Logout 활성화
        http
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(authSuccessHandler) // 로그인 성공시 안에 지정한 위치로 이동
                        .failureHandler(authFailureHandler) // 로그인 실패시 안에 지정한 위치로 이동
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout") // 변경 필요. 현재 404
                        .logoutSuccessUrl("/login")
                );


        // permit, authenticated 경로 설정
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        // 지정한 경로는 인증 없이 접근 허용
                        .anyRequest().authenticated());
        // 나머지 모든 경로는 인증 필요

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}