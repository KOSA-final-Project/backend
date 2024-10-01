# Latteve(라떼브)
### 개발자 사이드 프로젝트 모집 + 포트폴리오 구축 지원 플랫폼
### Latteve?
커피챗에서 영감을 받은 라떼, 즉 편안한 대화와 개발자를 의미하는 dev를 합한 단어입니다.


간편한 사이드 프로젝트 인원 모집과 미리 설정한 회고 주기마다 회고를 작성해서 프로젝트가 완성되면 하나의 포트폴리오를 완성해봐요. 다른 회원들과 커피챗(1:1 채팅)도 할 수 있어요!

## 주요기능
### 1. 회원기능
  - 소셜 로그인(구글, 카카오)
  - 마이페이지
### 2. 프로젝트 기능
  - 프로젝트 생성
  - 프로젝트 상세 조회
  - 프로젝트 지원
  - 프로젝트 회고
  - 프로젝트 좋아요
  - 프로젝트 관리(멤버 승인, 거절)
### 3. 검색 기능
  - 프로젝트 검색
  - 회원(라떼버) 검색
  - 통합 검색
### 4. 채팅 기능
  - 1:1 채팅(커피챗)

## 팀원 소개
|이름|역할|
|---|---|
|이주윤(c)|프로젝트 상세 페이지(정보탭, 회고탭)|
|이윤빈|프로젝트 아키텍처 구축, 로그인/회원가입, 채팅|
|장희선|검색 기능, 마이 페이지, 홈페이지|
|김영훈|프로젝트 아키텍처 구축, 프로젝트 생성 기능, 프로젝트 상세 페이지(관리탭)|

### 시스템 아키텍처
![14](https://github.com/user-attachments/assets/9ccbd8d6-f170-48d9-a4e0-444e40d403ab)
![15](https://github.com/user-attachments/assets/bfb73870-1324-468e-9789-f58796bee736)
- AWS를 기반으로 배포와 운영.
- GitHub Actions와 CodeDeploy를 통해 배포 자동화를 구현
- 블루/그린 방식을 통한 무중단 배포
- 스프링 서버에서는 회원 및 프로젝트 관련 기능을 담당하며 노드 서버에서는 채팅 및 알람 관련 기능 담당
- 프론트엔드는 Vercel을 이용하여 배포
- 스프링은 MySQL, 노드는 MongoDB를 이용하고 있어 데이터 동기화를 위해 rabbitMQ를 사용
- JWT Refresh token 탈취 방지를 위해 redis를 사용
- 검색 성능을 위해 Open search 사용
- 
