//package kdt.hackathon.applysecurity.config;
//
//import kdt.hackathon.applysecurity.jwt.JwtAuthenticationEntryPoint;
//import kdt.hackathon.applysecurity.jwt.JwtAuthenticationFilter;
//import kdt.hackathon.applysecurity.jwt.JwtTokenProvider;
//
//import kdt.hackathon.applysecurity.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
//import kdt.hackathon.applysecurity.oauth2.service.CustomOAuth2UserService;
//import kdt.hackathon.applysecurity.oauth2.handler.OAuth2AuthenticationFailureHandler;
//import kdt.hackathon.applysecurity.oauth2.handler.OAuth2AuthenticationSuccessHandler;
//
//
//import  kdt.hackathon.applysecurity.login.CustomJsonUsernamePasswordAuthenticationFilter;
//import  kdt.hackathon.applysecurity.login.LoginFailureHandler;
//import  kdt.hackathon.applysecurity.login.LoginSuccessHandler;
//import  kdt.hackathon.applysecurity.login.LoginService;
//
//import  kdt.hackathon.applysecurity.repository.MemberReopository;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.logout.LogoutFilter;
//import java.io.IOException;
//
//@Configuration
//@Log4j2
//public class CustomSecurityConfig {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 추가
//                .formLogin(formLogin -> formLogin.disable())    // FormLogin 사용 x
//                .httpBasic(httpBasic -> httpBasic.disable())    // httpBasic 사용 x
//                .csrf(csrf -> csrf.disable())
//
//                // 세션 사용하지 않으므로 STATELESS로 설정
//                .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//
//                //== URL별 권한 관리 옵션 ==//
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers("/", "/index.html","/css/**", "/images/**").permitAll()  // 기본 페이지, static 하위 폴더에 있는 자료들은 모두 접근 가능
//                        .requestMatchers("/api/swagger-ui.html", "/api/v3/api-docs/**", "/swagger-resources/**", "/api/swagger-ui/**").permitAll()
//                        .requestMatchers("/api/user/signup", "/api/user/oauthSignup").permitAll()   // 회원가입 접근 가능
//                        .requestMatchers("/api/user/login", "/api/user/refresh").permitAll()     // 로그인 접근 가능
//                        .requestMatchers("/api/user/nicknameCheck", "/api/user/emailCheck", "/api/user/mail/sendVerification", "/api/user/mail/verifyCode",
//                                "/api/user/pw/sendVerification", "/api/user/pw/verifyCode").permitAll() // 회원가입, 로그인 관련 인증
//                        .requestMatchers("/api/tags/**").permitAll()
//                        .requestMatchers("/api/ws/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//
//                //== 소셜 로그인 설정 ==//
//                .oauth2Login(oauth2 -> oauth2
//                        .authorizationEndpoint(auth -> auth
//                                .baseUri("/oauth2/authorize")
//                                .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository())
//                        )
//                        .redirectionEndpoint(redir -> redir
//                                .baseUri("/login/oauth2/code/**")
//                        )
//                        .userInfoEndpoint(userinfo -> userinfo
//                                .userService(customOAuth2UserService)   // customUserService 설정
//                        )
//                        .successHandler(oAuth2AuthenticationSuccessHandler)
//                        .failureHandler(oAuth2AuthenticationFailureHandler)
//                )
//                .exceptionHandling(e-> e
//                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
//                // 원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
//                // 따라서, LogoutFilter 이후에 우리가 만든 필터 동작하도록 설정
//                // 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
//                .addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class)
//                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    /**
//     * AuthenticationManager 설정 후 등록
//     * PasswordEncoder를 사용하는 AuthenticationProvider 지정 (PasswordEncoder는 위에서 등록한 PasswordEncoder 사용)
//     * FormLogin(기존 스프링 시큐리티 로그인)과 동일하게 DaoAuthenticationProvider 사용
//     * UserDetailsService는 커스텀 LoginService로 등록
//     * 또한, FormLogin과 동일하게 AuthenticationManager로는 구현체인 ProviderManager 사용(return ProviderManager)
//     *
//     */
//    @Bean
//    public AuthenticationManager authenticationManager() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(encoder());
//        provider.setUserDetailsService(loginService);
//        return new ProviderManager(provider);
//    }
//
//    /**
//     * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
//     */
//    @Bean
//    public LoginSuccessHandler loginSuccessHandler() {
//        return new LoginSuccessHandler(jwtTokenProvider, userRepository, objectMapper) {
//            @Override
//            protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//                String targetUrl = determineTargetUrl(request, response);
//                if (response.isCommitted()) {
//                    return;
//                }
//                getRedirectStrategy().sendRedirect(request, response, targetUrl);
//            }
//        };
//    }
//
//    /**
//     * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
//     */
//    @Bean
//    public LoginFailureHandler loginFailureHandler() {
//        return new LoginFailureHandler() {
//            @Override
//            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
//                getRedirectStrategy().sendRedirect(request, response, "/login?error");
//            }
//        };
//    }
//
//    /**
//     * CustomJsonUsernamePasswordAuthenticationFilter 빈 등록
//     * 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
//     * setAuthenticationManager(authenticationManager())로 위에서 등록한 AuthenticationManager(ProviderManager) 설정
//     * 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한 handler 설정
//     */
//    @Bean
//    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
//        CustomJsonUsernamePasswordAuthenticationFilter customFilter = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
//        customFilter.setAuthenticationManager(authenticationManager());
//        customFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
//        customFilter.setAuthenticationFailureHandler(loginFailureHandler());
//        return customFilter;
//    }
//
//}
