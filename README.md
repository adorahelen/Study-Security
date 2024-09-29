https://docs.spring.io/spring-security/site/docs/current/api/

# 로그인 방식의 종류 
<img width="623" alt="image" src="https://github.com/user-attachments/assets/d96becce-b677-47eb-96ac-f978e62298b4">

- FormLogin 은 주로 세션(스프링 시큐리티 기본 제공)과 함께 쓰임 
- 소셜 로그인은 카카오나, 구글, 네이버 등에게 인증 작업을 맡기는 것
    * 이때 인증 후 처리를 세션으로 할지, 토큰으로 할지는 커스텀에 따라 다르다. (세션/토큰/쿠기)
 - 로그인 방식은 "사용자가 직접 하거나", "다른 업체에게 맡기는 간접 방식 존재" [폼 로그인 <-> 소셜 로그인]
 - 로그인 후, 인증 정보를 서버에 저장하는 방식 Stateful (상태유지)
 - 로그인 후, 인증 정보를 서버에 저장하지 않는 방식 Statless (무상태)
 - 스테이트풀 방식은 세션, 스테이트리스 방식은 토큰이었다.
 - 세션 역시 레디스와 결합해서 스테이트 풀 방식의 단점(서버 부하)를 최소화 하기도 하며,
 - 토큰 역시 안정성을 높이기위해 리프레쉬토큰, 블랙리스트 등을 통해 부분적으로 상태를 저장하기도 한다.
    * 역설적이게도 이러면 서로가 점점 서로의 방식으로 역전되는 상황이 발생하게 됨
  
## 회원가입/로그인 방식 설계도 
<img width="901" alt="image" src="https://github.com/user-attachments/assets/7173c42f-860e-47d2-811a-2f7aa6f9d082">

- 어떻게 구현하냐에 따라서, 세션 방식 || 토큰 방식 달라짐 

## 세션방식 아키텍처
<img width="901" alt="image" src="https://github.com/user-attachments/assets/e9e5ae69-9a8a-45e2-b60c-cfdb86104513">
<img width="967" alt="image" src="https://github.com/user-attachments/assets/25448e9a-3769-4662-a0cb-359aecd24d9c">


- 위 사진은, 스프링 시큐리티에서 제공하는 기본적인 세션 기반의 로그인 아키텍처이다.

## 토큰방식 아키텍처
<img width="936" alt="image" src="https://github.com/user-attachments/assets/d143a69f-2ed0-4eae-bbb8-f9afb3b3ff76">

- 위 사진은, REST api + jwt 아키텍처

## 세션x, 토큰 아키텍처
<img width="623" alt="image" src="https://github.com/user-attachments/assets/e1046150-4bb3-401f-a78e-1c3c46b072cc">

