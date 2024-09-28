//package kdt.hackathon.applysecurity.config;
//
//
//import kdt.hackathon.applysecurity.jwt.JwtAuthenticationFilter;
//import kdt.hackathon.applysecurity.jwt.JwtTokenProvider;
//import kdt.hackathon.applysecurity.login.CustomAuthenticationFailureHandler;
//import kdt.hackathon.applysecurity.security.CustomUserDetailsService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@RequiredArgsConstructor
//@Configuration
//@Log4j2
//@EnableWebSecurity
//public class CustomSecurityConfig {
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//       // log.info("테스트");
//
//        http.csrf(csrf -> csrf.disable())
//
//                .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.NEVER))
//
//
//                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers("/admin").hasRole("ADMIN")
//                        .requestMatchers("/service").authenticated()
//                        .anyRequest().permitAll()
//                )
//
//                .formLogin()
//                .loginPage("/login")
//                .defaultSuccessUrl("/service")
//                .failureHandler(new CustomAuthenticationFailureHandler())
//                .permitAll()
//
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//                .deleteCookies("JSESSIONID")
//                .logoutSuccessUrl("/login");
//
//        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),UsernamePasswordAuthenticationFilter.class);
//        // @Bean
//        // public AuthenticationManager -> JwtAuthenticationFilter
//        // : 기존 세션 기반 인증 방식에서, 로그인 요청을 처리하고 세션을 생성하는 역할
//        //   ->  토큰을 이용해 요청이 들어올 때마다 해당 토큰의 유효성을 검증하고, 인증 정보를 설정하는 방식
//
//
//        return http.build();
//    }
//// =========================================================
//
//
//    @Bean // security default 2 등록
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean // jwt 1 등록
//    public JwtAuthenticationFilter tokenAuthenticationFilter() {
//        return new JwtAuthenticationFilter(jwtTokenProvider);
//    }
//
//}
