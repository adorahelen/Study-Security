package kdt.hackathon.applysecurity.jwt.config;

import kdt.hackathon.applysecurity.entity.UserDTO;
import kdt.hackathon.applysecurity.jwt.controller.dto.JwtInfoDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static java.lang.System.currentTimeMillis;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;

//    private final Long accessTokenExpireTime;
//    private final Long refreshTokenExpireTime;

    @Getter  // Lombok을 사용해 getter 자동 생성
    private final Long accessTokenExpireTime;

    @Getter  // refreshTokenExpireTime에도 적용 가능
    private final Long refreshTokenExpireTime;


    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);


    // application.properties 에 저장된 키 및 시간
    //      * Caused by: io.jsonwebtoken.security.WeakKeyException: 키 값이 너무 짧습니다.
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.access_expiration_time}") Long accessTokenExpireTime,
                   @Value("${jwt.refresh_expiration_time}") Long refreshTokenExpireTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    } // 생성자
    // 설정파일에서 바인딩해놓은 secret와 access_expiration_time, refresh_expiration_time을 사용

    /**
     * accessToken, refreshToken을 생성한다.
     * 컴포넌트 내의 createAccessToken, createRefreshToken을 호출하여 생성한다.
     * @param userInfoDto
     * @return JwtInfoDto
     */
    // 1. 엑세스 토큰&리프레쉬 토큰 : 발행 (토큰 서비스에서 호출)
    public JwtInfoDto createToken(UserDTO userInfoDto) {

        Date accessTokenExpirationTime = new Date(currentTimeMillis() + accessTokenExpireTime);
        Date refreshTokenExpirationTime = new Date(currentTimeMillis() + refreshTokenExpireTime);

        String accessToken = createAccessToken(userInfoDto, accessTokenExpirationTime);
        String refreshToken = createRefreshToken(userInfoDto, refreshTokenExpirationTime);

        logger.info("토큰 생성 완료: 액세스 토큰 만료 시간은 {}입니다.", accessTokenExpirationTime);
        return JwtInfoDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpirationTime)
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(refreshTokenExpirationTime)
                .build();
    }

    /**
     * accessToken 생성
     * @param userInfoDto
     * @param expirationTime
     * @return accessToken
     */

    // 1-1 엑세스 토큰 발행 : private -> public, use ReissueService
    public String createAccessToken(UserDTO userInfoDto, Date expirationTime) {
        logger.debug("액세스 토큰 생성 중: 사용자 정보 - 이메일: {}, 역할: {}", userInfoDto.getEmail(), userInfoDto.getRole());
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject("AccessToken")
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .claim("UserId", userInfoDto.getId())
                .claim("email", userInfoDto.getEmail())
                .claim("role", userInfoDto.getRole())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * refreshToken 생성
     * @param userInfoDto
     * @param expirationTime
     * @return
     */

    // 1-2 리프레쉬 토큰 발행 :
    private String createRefreshToken(UserDTO userInfoDto, Date expirationTime) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject("RefreshToken")
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .claim("UserId", userInfoDto.getId())
                .claim("email", userInfoDto.getEmail())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * token을 파싱하여 email을 리턴
     * @param token
     * @return email
     */

    // 2. 토큰에서 이메일을 추출(필터에서 사용)
    public String getEmail(String token) {
        return parseClaims(token).get("email", String.class);
    }

    /**
     * 해당 token이 유효한지 체크
     * @param token
     * @return
     */

    // 3. 토큰 유효성 검증(필터에서 사용)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("JWT 토큰이 유효하지 않습니다.", e);
        } catch (ExpiredJwtException e) {
            log.info("JWT 토큰이 만료되었습니다.", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT 토큰 입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims가 비어있습니다.", e);
        }
        return false;
    }

    // 2-1 (토큰, 이메일 추출시 사용)
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 4.
    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.get("UserId", Long.class);
    }

    // 5.
    public Long getAccessTokenExpireTime() {
        return this.accessTokenExpireTime;
    }


    //토큰 기반으로 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token); // 토큰에서 클레임을 가져옴
        //기본적으로 "ROLE_USER" 권한을 부여
        Set<SimpleGrantedAuthority> authorities= Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(
                claims.getSubject(),"",authorities),token,authorities);
        //Authentication 객체를 생성
        // 이 객체는 Spring Security에서 사용자 인증 정보를 저장하고 관리하는 데 사용됩니다.
    }
}
