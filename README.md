https://docs.spring.io/spring-security/site/docs/current/api/


# 로그인 방식의 종류 
<img width="623" alt="image" src="https://github.com/user-attachments/assets/d96becce-b677-47eb-96ac-f978e62298b4">

- FormLogin 은 주로 세션(스프링 시큐리티 기본 제공)과 함께 쓰임 
- 소셜 로그인은 카카오나, 구글, 네이버 등에게 인증 작업을 맡기는 것
    * 이때 인증 후 처리를 세션으로 할지, 토큰으로 할지는 커스텀에 따라 다르다. (세션/토큰/쿠기)
 - 로그인 방식은 "사용자가 직접 하거나", "다른 업체에게 맡기는 간접 방식 존재" [폼 로그인 <-> 소셜 로그인]
 - 로그인 후, 인증 정보를 서버에 저장하는 방식 Stateful (상태유지) : 세션
 - 로그인 후, 인증 정보를 서버에 저장하지 않는 방식 Statless (무상태) : 토큰
    * 쿠키는 브라우저에 저장(세션,토큰 둘다 가능)
    * 세션 역시 레디스와 결합해서 스테이트 풀 방식의 단점(서버 부하)를 최소화 하기도 하며,
    * 토큰 역시 안정성을 높이기위해 리프레쉬토큰, 블랙리스트 등을 통해 부분적으로 상태를 저장하기도 한다.
    * 역설적이게도 이러면 서로가 점점 서로의 방식으로 역전되는 상황이 발생하게 됨
  
- 목차
    * 프론트: html, form 태그(@ModelAttribute) -> (스프링 시큐리티 자동/ 세션저장)
    * 프론트: js, JSON 데이터(@RequestBody) -> (수동으로 재정의 필수/ 세션저장)
    * 프론트: js, JSON 데이터(@RequestBody) -> (수동으로 재정의 필수/ jwt발행)
  
## (HTML form -> session)
<img width="901" alt="image" src="https://github.com/user-attachments/assets/3a446d9f-faa2-4e3b-89be-e1fdac018696">
<img width="901" alt="image" src="https://github.com/user-attachments/assets/e9e5ae69-9a8a-45e2-b60c-cfdb86104513">
<img width="901" alt="image" src="https://github.com/user-attachments/assets/7173c42f-860e-47d2-811a-2f7aa6f9d082">

- 위 사진은 전부 스프링시큐리티에게 위임 (자동)
- 구현 내용
    * 뷰페이지 띄우기
    * 회원가입 로직
    * implemets : UserDetail, UserDetailService
    * front : html, { form action="/login" method="POST" }

## (JSON data -> session)
	1.	로그인 요청: 사용자가 ID와 비밀번호를 입력하고 서버에 POST 요청으로 로그인 데이터를 전송합니다.
	2.	인증 처리: 서버는 전달받은 로그인 데이터를 확인하고, 사용자의 자격 증명이 올바르다면 세션을 생성하여 사용자를 인증합니다.
	3.	세션 생성: 인증이 성공하면 서버는 세션을 생성하고, 해당 세션을 식별하는 세션 ID를 클라이언트에 쿠키로 전달합니다.
	4.	쿠키 저장: 클라이언트는 서버로부터 받은 세션 ID를 브라우저의 쿠키에 저장합니다.
	5.	요청 시 세션 확인: 이후 요청마다 클라이언트는 이 세션 ID를 함께 보내고, 서버는 이를 확인하여 해당 세션이 유효한지 확인하고, 유효하다면 사용자가 인증된 상태로 요청을 처리합니다.
	6.	로그아웃: 사용자가 로그아웃하면 서버는 세션을 삭제하거나 무효화합니다.


<img width="901" alt="image" src="https://github.com/user-attachments/assets/64612a2d-cd92-41e0-8b08-5c9fa69f8080">
<img width="901" alt="image" src="https://github.com/user-attachments/assets/03d3a319-c911-4aa3-b017-fee533e8f1fa">
<img width="901" alt="image" src="https://github.com/user-attachments/assets/25448e9a-3769-4662-a0cb-359aecd24d9c">
<img width="901" alt="image" src="https://github.com/user-attachments/assets/7e2f20bb-d0c2-4a73-9289-af92c34320a7">


- 위 사진은 스프링 시큐리티를 조정 (수동)
- implemets : "CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter"
    * AuthenticationManager 주입, @Override Authentication { authenticationToken,
    *   SecurityContextHolder.getContext().setAuthentication(authResult); }
- front : js, fetch( {method: 'POST',headers: { "Content-Type": "application/json",}, body: ~Json~
- 필터가 핵심, 나머지는 그 안에 들어있음 || 넣을 수 있음
  
<img width="901" alt="image" src="https://github.com/user-attachments/assets/6aa65fb0-8bfe-4382-a8a2-932a556eb20b">

- UserDetails는 자신이 사용할 엔티티에 username, pssword, Authorization 등의 필드를 추가하여 폼로그인을 지원하도록 하는 인터페이스
- AuthenticationManger에서 인증이 완료되면 Authentication객체를 반환하여 SecurityContext에 저장
- SecurityContext는 현재 인증된 사용자와 관련된 모든 정보를 보유
- SecurityContextHolder를 통해 접근 가능하며, Authentication객체를 저장하고 이를 통해 인증된 사용자 정보를 제공

## (JSON data -> jwt)
	1.	로그인 요청: 사용자가 ID와 비밀번호를 입력하고 서버에 POST 요청으로 로그인 데이터를 전송합니다.
	2.	인증 처리: 서버는 전달받은 로그인 데이터를 확인하고, 사용자의 자격 증명이 올바르다면 JWT(토큰)를 생성하여 클라이언트에 반환합니다.
	3.	토큰 전달: 클라이언트는 JWT를 받아서 로컬 스토리지나 세션 스토리지 또는 쿠키에 저장합니다. 이 JWT는 클라이언트에 전적으로 저장되며, 서버는 이를 저장하지 않습니다.
	4.	요청 시 토큰 포함: 이후 요청마다 클라이언트는 HTTP 헤더(보통 Authorization: Bearer <token>)에 JWT를 포함하여 서버에 요청을 보냅니다.
	5.	토큰 검증: 서버는 JWT를 검증하여 그 유효성을 확인하고, 유효한 경우 사용자를 인증된 상태로 요청을 처리합니다.
	6.	로그아웃: 클라이언트 측에서 JWT를 삭제하면 로그아웃 처리가 됩니다. 서버는 상태를 저장하지 않기 때문에 별도로 세션을 만료시킬 필요는 없습니다.


- 세션과 토큰의 차이

<img width="901" alt="image" src="https://github.com/user-attachments/assets/529905ce-761d-4b65-99c6-5b6eeb32fa14">

<img width="901" alt="image" src="https://github.com/user-attachments/assets/34c98069-378a-46a5-b4bb-ac96dd07c415">
<img width="901" alt="image" src="https://github.com/user-attachments/assets/d143a69f-2ed0-4eae-bbb8-f9afb3b3ff76">

- 기본적으로 세션 + 쿠키를 사용하는 Spring Security의 Form Login
- JWT인증 방식을 통해 세션을 사용하지 않고 Form Login을 구현 가능 (토큰-> 쿠키)
    * 클라이언트와 서버 간의 Stateless인증을 가능하게 함 -> Stateful인증을 사용하는 세션의 대안
    * jwt의 버전에 따라 JWT를 빌드하는 메서드가 달라질 수 있음

- UsernamePasswordAuthenticationFilter에서의 인증 대신 JWT를 사용하여 인증을 진행하는 커스텀 필터인 JwtAuthFilter를 생성
- JwtAuthFilter에서 헤더에 담긴 accessToken을 파싱하여 유효성 검증 후, accessToken으로 Authentication 객체를 생성하여 인증정보를 관리하는 SecurityContext에 넣어 인증을 완료
    * UsernamePasswordAuthenticationFilter에서 -> 당 역할을 JwtAuthenticationFilter에 위임
 
- 무중단 서비스 운영을 위한, 엑세스 토큰 만료시 리프레쉬 토큰 발급 절차 (실시간 확인 후 발급)
    * 클라이언트가 서버로 인증이 필요한 API 요청을 보낼 때, 엑세스 토큰이 만료되었을 경우
    *  <= 서버에서 401(Unauthorized) 상태 코드를 응답 & 아래와 같은 과정을 거쳐 새로운 엑세스 토큰을 발급

  	1.	Step 1: 클라이언트가 서버로 요청을 보낼 때 엑세스 토큰이 만료되었다고 응답(401)을 받음.
	2.	Step 2: 클라이언트가 리프레시 토큰을 이용해 /api/v1/jwt/reissue 엔드포인트로 새로운 엑세스 토큰을 요청.
	3.	Step 3: 서버가 새로운 엑세스 토큰을 발급하고 클라이언트에게 응답.
	4.	Step 4: 클라이언트가 재발급받은 엑세스 토큰을 저장한 후, 실패했던 요청을 다시 재시도.

- 주기 갱신 방법(만료 시간이 가까워지면, 발급을 강제 시킴?)
  	1.	Step 1: 클라이언트는 엑세스 토큰의 만료 시간을 추적.
	2.	Step 2: 만료 시간이 가까워졌을 때 /api/v1/jwt/reissue로 요청을 보내 새로운 토큰을 발급받음.
	3.	Step 3: 발급받은 새로운 엑세스 토큰으로 클라이언트의 세션을 유지.

- 요약 : 엑세스 토큰이 만료되어 재발급되는 과정은 토큰 만료 시점에 서버로 요청을 보낼 때 발생
    * 이때 401 응답을 확인하고 발급 || 만료 시간을 추적하여 미리 갱신 

