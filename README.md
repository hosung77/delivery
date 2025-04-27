# 배달 서비스 프로젝트

## 1. 프로젝트 소개

스프링 부트 기반의 배달 서비스 관리 웹 페이지

## 2. 개발 기간 및 개발자

개발 기간 : 25.04.22 ~ 25.04.29

개발자 : 이호성(팀장), 김동희, 조효준, 송윤태

역할 
 - 이호성 : 장바구니,주문 CRUD 및 AOP 로깅, API Doc, 형상 관리 (PR)
 - 김동희 : ERD, 토큰 인증 로그인, 메뉴 CRUD, 개발 환경 세팅
 - 조효준 : 가게 CRUD, API Doc
 - 송윤태 : 리뷰 CRUD, API Doc
## 3. 개발 환경

사용 기술 : Java SpringBoot, Git, Github, Docker

자바 버전 : 17

빌드 : Gradle

DB : DockerCompose, mysql 8.0.36, JPA

ERD 설계 : https://www.erdcloud.com/d/sNjynvbKRWMRZ4pSc

API Doc : https://teamsparta.notion.site/API-1d62dc3ef51481a282a6c488645e0c41


## 4. 주요 기능

### 유저
1. 가게를 조회하여 메뉴를 골라 장바구니를 통해 주문할 수 있다.
3. 주문을 통해 리뷰를 작성할 수 있다.


### 사장
1. 가게를 생성하고 등록할 수 있다. 
2. 메뉴를 추가할 수 있다.
3. 가게와 메뉴를 관리할 수 있다.