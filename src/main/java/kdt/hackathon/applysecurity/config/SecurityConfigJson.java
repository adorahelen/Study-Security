//package kdt.hackathon.applysecurity.config;
//
//import kdt.hackathon.applysecurity.jsonLogin.CustomAuthenticationFilter;
//import kdt.hackathon.applysecurity.security.CustomUserDetailsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfigJson {
//
//    private final CustomUserDetailsService userDetailsService;
//
//    // 1. 스프링 시큐리티에서 특정 리소스 무시 설정
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring()
//                .requestMatchers("/static/**"); // static 리소스에 대한 시큐리티 비활성화
//    }
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//
//        // AuthenticationManager 설정
//        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
//
//        // CustomAuthenticationFilter 추가
//        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager);
//        customAuthenticationFilter.setFilterProcessesUrl("/api/login"); // 로그인 엔드포인트 설정
//
//        // csrf 비 활성화
//        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
//
//
//        return http
//                .csrf().disable() // CSRF는 Fetch 방식이므로 설정을 해제하거나 커스터마이즈
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .invalidSessionUrl("/login?invalid-session")
//                .and()
//                .addFilter(new CustomAuthenticationFilter(authenticationManager))
//
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll() // 모든 요청 허용
//                )
//                .build();
//    }
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//
//
//}