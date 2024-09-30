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
<img width="901" alt="image" src="https://github.com/user-attachments/assets/64612a2d-cd92-41e0-8b08-5c9fa69f8080">
<img width="901" alt="image" src="https://github.com/user-attachments/assets/03d3a319-c911-4aa3-b017-fee533e8f1fa">
<img width="901" alt="image" src="https://github.com/user-attachments/assets/25448e9a-3769-4662-a0cb-359aecd24d9c">

- 위 사진은 스프링 시큐리티를 조정 (수동)
- implemets : "CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter"
    * AuthenticationManager 주입, @Override Authentication { authenticationToken,
    *   SecurityContextHolder.getContext().setAuthentication(authResult); }
- front : js, fetch( {method: 'POST',headers: { "Content-Type": "application/json",}, body: ~Json~


## (JSON data -> jwt)
<img width="901" alt="image" src="https://github.com/user-attachments/assets/34c98069-378a-46a5-b4bb-ac96dd07c415">
<img width="901" alt="image" src="https://github.com/user-attachments/assets/d143a69f-2ed0-4eae-bbb8-f9afb3b3ff76">

- 정리 중


