package kdt.hackathon.applysecurity.jwt.config;

import kdt.hackathon.applysecurity.entity.UserDTO;
import kdt.hackathon.applysecurity.jwt.controller.dto.JwtInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final Long accessTokenExpireTime;
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
    }

    /**
     * accessToken, refreshToken을 생성한다.
     * 컴포넌트 내의 createAccessToken, createRefreshToken을 호출하여 생성한다.
     * @param memberInfoDto
     * @return JwtInfoDto
     */
    public JwtInfoDto createToken(UserDTO memberInfoDto) {

        Date accessTokenExpirationTime = new Date(currentTimeMillis() + accessTokenExpireTime);
        Date refreshTokenExpirationTime = new Date(currentTimeMillis() + refreshTokenExpireTime);

        String accessToken = createAccessToken(memberInfoDto, accessTokenExpirationTime);
        String refreshToken = createRefreshToken(memberInfoDto, refreshTokenExpirationTime);

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
     * @param memberInfoDto
     * @param expirationTime
     * @return accessToken
     */
    private String createAccessToken(UserDTO memberInfoDto, Date expirationTime) {
        logger.debug("액세스 토큰 생성 중: 사용자 정보 - 이메일: {}, 역할: {}", memberInfoDto.getEmail(), memberInfoDto.getRole());
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject("AccessToken")
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
             //   .claim("memberId", memberInfoDto.getMemberId())
                .claim("email", memberInfoDto.getEmail())
                .claim("role", memberInfoDto.getRole())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * refreshToken 생성
     * @param memberInfoDto
     * @param expirationTime
     * @return
     */
    private String createRefreshToken(UserDTO memberInfoDto, Date expirationTime) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject("RefreshToken")
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
             //   .claim("memberId", memberInfoDto.getMemberId())
                .claim("email", memberInfoDto.getEmail())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * token을 파싱하여 email을 리턴
     * @param token
     * @return email
     */
    public String getEmail(String token) {
        return parseClaims(token).get("email", String.class);
    }

    /**
     * 해당 token이 유효한지 체크
     * @param token
     * @return
     */
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

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
