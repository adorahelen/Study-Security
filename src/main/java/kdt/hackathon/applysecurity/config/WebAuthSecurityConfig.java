//package kdt.hackathon.applysecurity.config;
//
//import kdt.hackathon.practicesecurity.config.jwt.TokenProvider;
//import kdt.hackathon.practicesecurity.config.ouath.OAuth2SuccessHandler;
//import kdt.hackathon.practicesecurity.repository.RefreshTokenRepository;
//import kdt.hackathon.practicesecurity.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.HttpStatusEntryPoint;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@RequiredArgsConstructor
//@Configuration
//public class WebAuthSecurityConfig extends DefaultOAuth2UserService {
//
//    private final TokenProvider tokenProvider;
//    private final RefreshTokenRepository refreshTokenRepository;
//    private final UserRepository userRepository;
//    private final OAuth2UserCustomService oAuth2UserCustomService;
//
//    @Bean // 몇몇의 예외처리에는 스프링 시큐리티 기능 비활성화
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring()
//                .requestMatchers("/img/**", "/css/**", "/js/**", "/fonts/**", "/images/**", "/webjars/**");
//    }
//
//    @Bean // 토큰 방식으로 로그인 하기에, 세션 비활성화 설정 ( 폼 로그인도 )
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .httpBasic().disable()
//                .formLogin().disable()
//                .logout().disable();
//
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        // 커스텀 토큰 필터 추가
//        http.addFilterBefore(
//                tokenAuthenticationFilter(),
//                UsernamePasswordAuthenticationFilter.class);
//
//        // 토큰 재발급 URL 은 인증 없이 접근 가능하도록 설정, 나머지 API URL 은 인증 필요
//        http.authorizeRequests()
//                .requestMatchers("/api/token").permitAll()
//                .requestMatchers("/api/**").authenticated()
//                .anyRequest().permitAll();
//
//        // OAuth2 로그인 설정
//        http.oauth2Login(oauth2 -> oauth2
//                .loginPage("/login")
//                .authorizationEndpoint(authorization -> authorization
//                        .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))
//                .successHandler(oAuth2SuccessHandler())
//                .userInfoEndpoint(userInfo -> userInfo
//                        .userService(oAuth2UserCustomService))); // OAuth2UserCustomService 통합된 서비스 사용
//
//        http.logout().logoutSuccessUrl("/login");
//
//        // 예외 핸들링 처리
//        http.exceptionHandling()
//                .defaultAuthenticationEntryPointFor(
//                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
//                        new AntPathRequestMatcher("/api/**"));
//
//        return http.build();
//    }
//
//    @Bean
//    public OAuth2SuccessHandler oAuth2SuccessHandler() {
//        return new OAuth2SuccessHandler(
//                tokenProvider,
//                refreshTokenRepository,
//                oAuth2AuthorizationRequestBasedOnCookieRepository(),
//                userRepository
//        );
//    }
//
//    @Bean
//    public TokenAuthenFilterConfig tokenAuthenticationFilter() {
//        return new TokenAuthenFilterConfig(tokenProvider);
//    }
//
//    @Bean
//    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
//        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
//    }
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}