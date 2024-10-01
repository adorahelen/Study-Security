//package kdt.hackathon.applysecurity.jsonLogin;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import java.io.IOException;
//import java.util.Map;
//
//@RequiredArgsConstructor
//public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final AuthenticationManager authenticationManager;
//    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFilter.class);
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
//            throws AuthenticationException {
//
//        // JSON 데이터 파싱
//        ObjectMapper objectMapper = new ObjectMapper();
//        UsernamePasswordAuthenticationToken authenticationToken;
//
//        try {
//            Map<String, String> credentials = objectMapper.readValue(request.getInputStream(), Map.class);
//            String username = credentials.get("username");
//            String password = credentials.get("password");
//
//            logger.debug("Parsed credentials: username={}, password={}", username, password);
//
//            if (username == null || password == null) {
//                logger.error("Username or password is null");
//                throw new RuntimeException("Username or password cannot be null");
//            }
//
//            authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
//        } catch (IOException e) {
//            logger.error("Failed to parse login request body: {}", e.getMessage());
//            throw new RuntimeException("Failed to parse login request body", e);
//        }
//
//        logger.debug("Attempting authentication for user: {}", authenticationToken.getName());
//        return authenticationManager.authenticate(authenticationToken);
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
//        logger.error("Authentication failed: {}", failed.getMessage());
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write("Authentication failed: " + failed.getMessage());
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
//        logger.info("User {} authenticated successfully", authResult.getName());
//        SecurityContextHolder.getContext().setAuthentication(authResult); // 인증 정보를 SecurityContext에 저장
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.getWriter().write("Login successful for user: " + authResult.getName());
//    }
//
//    // 필터 경로 설정 추가 (필요 시 SecurityConfig에서 호출)
//    public void setFilterProcessesUrl(String loginUrl) {
//        super.setFilterProcessesUrl(loginUrl);
//    }
//}