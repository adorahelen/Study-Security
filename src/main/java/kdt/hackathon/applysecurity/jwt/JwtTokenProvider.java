//package kdt.hackathon.applysecurity.jwt;
//
//import io.jsonwebtoken.*;
//import kdt.hackathon.applysecurity.entity.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.util.Collections;
//import java.util.Date;
//import java.util.Set;
//
//@RequiredArgsConstructor
//@Service
//public class JwtTokenProvider {
//
//    private final JwtProperties jwtProperties;
//
//    public String generateToken(User user, Duration duration) {
//        Date now = new Date();
//        return makeToken(new Date(now.getTime() + duration.toMillis()), user);}
//
//    // 1. JWT 토큰 생성
//    private String makeToken(Date expiry, User user) {
//
//        Date now = new Date();
//
//        return Jwts.builder()
//                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 토큰 헤더
//                .setIssuer(jwtProperties.getIssuer()) // 토큰 이슈(내용), 프로퍼티스에서 작성한 내 메일
//                .setIssuedAt(now) // 발급시간 = 현재시간
//                .setExpiration(expiry) // 만기일 = 변수값(만기변수)
//                .setSubject(user.getEmail()) // 유저 이메일 내용에 넣음 -> 전화번호가 키 (아마 여긴 이메일)
//                .claim("id", user.getId()) // 클레임 아이디에 유저 아이디 삽입
//                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) // 서명 : 비밀값 + 해시값을, H256방식으로 암호
//                .compact();
//    }
//
//    // 2. JWT 토큰 유효성 검증
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser()
//                    .setSigningKey(jwtProperties.getSecretKey()) // 비밀값으로 복호화 (암호문->평문)
//                    .parseClaimsJws(token);
//            return true;
//        }catch (Exception e) { // 복화한걸 토큰과 비교했을 때 문제가 생기면, 유효하지 않은 토큰
//            return false;
//        }
//    }
//
//    // 3. 토큰 기반으로 인증 정보를 가져오는 메서드 ( = 토큰을 받아, 인증 정보를 담은 객체: Authentication 를 반환하는 메소드)
//
//    public Authentication getAuthentication(String token) {
//        Claims claims = getClaims(token); //  클레임에는 사용자 식별 정보가 포함(가져옴)
//
//        Set<SimpleGrantedAuthority> authorities // ROLE_USER라는 단일 권한을 설정
//                = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
//
//        return new UsernamePasswordAuthenticationToken // -> 스프링 시큐리티에서 제공하는 Authentication 인터페이스의 구현체
//                // 사용자 이름과 비밀번호 기반 인증을 나타내는데 사용
//
//                (new org.springframework.security.core.userdetails. //스프링 시큐리티의 User 객체를 생성
//                        User(claims.getSubject(), "", authorities), token, authorities);
//        // 	•	claims.getSubject()는 클레임에서 사용자 식별 정보(예: 사용자 ID)를 가져옵니다.
//        // 	•	두 번째 인자인 빈 문자열 ""는 비밀번호입니다. JWT 토큰에서 비밀번호는 사용하지 않기 때문에 빈 문자열로 처리됩니다.
//        // 	•	세 번째 인자: token은 JWT 토큰 자체입니다.
//        // 	•	네 번째 인자: authorities는 이 사용자가 가진 권한 목록입니다.
//
//    }
//
//
//    //    // 4. 토큰 기반으로 유저 ID를 가져오는
////    public Long getUserIdLongToken(String token) {
////        Claims claims = getClaims(token);
////        return claims.get("id", Long.class); // 게시판에서 조회를 위해 id 반환하는거랑 비슷함
////    }
//
//    // 4-2
//    public String getUserIdStringToken(String token) {
//        Claims claims = getClaims(token);
//        return claims.get("id", String.class);
//    } // 안쓰겠지만 헉시 모르니 일단 두가지 버전을 만들어 둠
//
//
//    // * 클레임 가져오는 메소드 (부가적임)
//    // 	•	JWT 토큰으로부터 클레임(claims)을 추출합니다. 클레임은 JWT에 포함된 사용자 정보나 메타데이터를 의미합니다.
//    //                                            클레임은 사용자 식별 정보(subject), 발행 시간, 만료 시간 등을 포함
//    private Claims getClaims(String token) {
//        return Jwts.parser() // 클레임 조회
//                .setSigningKey(jwtProperties.getSecretKey())
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//
//}
