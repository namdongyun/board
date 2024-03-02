# board

## 소개

Spring과 React를 사용하여 구현한 게시판과 실시간 채팅 기능을 갖춘 커뮤니티 웹 사이트입니다.

- 기존의 일반적인 커뮤니티 사이트와 달리, 사용자들에게 색다른 재미와 경험을 제공하고자 이 프로젝트를 시작하였습니다.
- 사용자 인증에는 JWT(Json Web Token)와 리프레시 토큰을 사용하여 높은 수준의 보안을 유지합니다.

## 주요 기능

- **게시판**: 사용자들이 글을 작성하고, 읽고, 댓글을 달 수 있는 기본적인 게시판 기능을 제공합니다.
- **실시간 채팅**: WebSocket을 활용하여 사용자들이 실시간으로 소통할 수 있는 채팅 기능을 제공합니다.
- **보안**: JWT와 리프레시 토큰을 통해 사용자 인증을 관리하며, 세션을 유지하는 동안 토큰 갱신을 자동으로 진행합니다. [code](https://github.com/namdongyun/board/blob/d905b597a83613060fbbdd241fe2385049c1eb90/src/main/board-react-app/src/axiosInterceptor/api.js#L8-L68)

## 기술 스택

### ✔️Front-End
<img src="https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=React&logoColor=black"/> <img src="https://img.shields.io/badge/MUI-007FFF?style=flat-square&logo=mui&logoColor=white"/>

### ✔️Back-End
<img src="https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=Spring&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white"> 

### ✔️Database
<img src="https://img.shields.io/badge/MariaDB-003545?style=flat-square&logo=mariaDB&logoColor=white"/>

#### 게시글 리스트
<img src="https://github.com/namdongyun/board/assets/77672125/dd325e6e-c28c-403e-a2de-452c23fab0d4" width="476" height="410"/>

#### 게시글 상세
<img src="https://github.com/namdongyun/board/assets/77672125/f02dd7f8-95a0-483d-adea-7c263b4ba053" width="476" height="410"/>

#### 실시간 채팅
<img src="https://github.com/namdongyun/board/assets/77672125/ca26a513-fc0f-45cf-81cc-cb5dd45ef253" width="476" height="410"/>

#### 마이페이지
<img src="https://github.com/namdongyun/board/assets/77672125/6f0e1f62-256a-4cf5-9273-f33e7a856134" width="476" height="410"/>
