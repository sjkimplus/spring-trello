# HOTSIX-TRELLO


## 🏁프로젝트 목표
"HOTSIX 먹고 힘내서 일하는 Trello 서비스 프로젝트!!!" 
이 문서는 Trello 프로젝트의 API 명세서, 주요 기능 및 트러블 슈팅 내용을 포함하고, 프로젝트의 목표는 사용자 친화적인 업무 일정 관리 툴을 제공하는 것입니다.
---

## 👨‍👩‍👧‍👦 Our Team

|김성주|박대현|임채규|정진호|
|:-:|:--:|:---:|:---:|

## 🏆 API 명세서

### 1️⃣ 회원가입/로그인 기능 및 TICKET 검색 기능 (담당 : 김성주)
![image](https://github.com/user-attachments/assets/035a73ad-cabd-4542-a26c-8654351401e7)

### 2️⃣ MEMBER 및 KANBAN 기능 (담당 : 임채규)
![image](https://github.com/user-attachments/assets/d07a4627-0601-41be-8ce3-274ebe8b9fee)

### 3️⃣ WORKSPACE 및 COMMENT 기능 (담당 : 정진호)
![image](https://github.com/user-attachments/assets/e79b3d2b-03f0-4aec-a625-062a0866ae04)
### 4️⃣ BOARD 및 TICKET 기능 (담당 : 박대현)
![image](https://github.com/user-attachments/assets/e04b7fd1-7458-40fd-a952-7eb30734c17c)
![image](https://github.com/user-attachments/assets/474f1826-d6ce-48c7-8ba3-d473595ab6e4)
![image](https://github.com/user-attachments/assets/06cdae45-329b-47cf-b583-0dbea5c75417)


---

## 📋 ERD Diagram
![image](https://github.com/user-attachments/assets/a565d799-53d6-4347-bde5-a607650a6580)


---
## 📋 와이어프레임
![image](https://github.com/user-attachments/assets/6c8e9fb1-df1c-4afc-8c54-44f431346d58)


---

## 프로젝트 핵심기능

### 🔔 첨부파일 등록

> * AMAZON S3를 이용하여, 티켓의 첨부파일을 등록하고, 수정, 삭제할 수 있는 기능을 구현 하였습니다.
> * 별도의 첨부파일 ENTITY를 생성하여 연관관계를 맺음으로써 하나의 티켓에 여러 개의 파일 첨부가 가능하도록 설계하였습니다.

### 📢 Slack 알림기능

> * Slack API를 이용하여, 특정 이벤트가 발생했을 때 Slack으로 알림을 받을 수 있습니다.
> * 워크스페이스에 새로운 멤버가 추가되었을 때, 해당 멤버가 어느 워크스페이스에 등록되었는지 알 수 있습니다
> * 티켓에 새로운 댓글이 등록되었을 때, 어느 티켓에 댓글이 등록되었는지 알 수 있습니다.

### 🗨 칸반 보드형식의 협업 툴 (CRUD)

> * 워크스페이스 내에 멤버들 끼리 할일을 정리하고 공유 할 수 있습니다.
> * 1.워크스페이스 작성/수정/삭제 : 워크스페이스 작성 후 같이 작업할 동료들을 초대할 수 있습니다.
> * 2.티켓, 댓글 작성/수정/삭제/조회 : 첨부파일 추가가 가능하며, 해당 할일을 맡을 매니저 등록이 가능합니다.
> * 3.조회수 : 티켓의 조회수를 기록하여 조회수에 따라 일간 랭킹을 알 수 있습니다.

### 🔍 검색 기능(내용,제목,마감일,담당자,보드ID 검색기능)

> * QueryDSL을 활용하여 동적 쿼리작성이 가능하도록 구현하였습니다.
> * ticket의 제목, 내용, 마감일, 담당자 및 보드ID 로 현재 유저가 들어가 있는 workspace 안에서 검색을 해 줍니다.

### 📊 동시성제어

> * 여러 사용자가 동시에 같은 티켓을 수정하는 경우에, 락을 사용하여 데이터 일관성을 지키도록 했습니다.
> * @Lock을 사용하는 비관락 방식을 채택하고 적용했습니다.
> * 낙관락과 비관락의 성능과 데이터일관성 보장도를 비교하기 위한 테스트코드가 첨부되어있습니다.

### 🚨 캐싱 (조회수 관리)
> * 연속된 요청으로 인한 DB병목을 해소하고 소멸기간이 존재하는 데이터의 TimeToLive 관리를 용이하게 할 수 있도록 Redis를 도입하였습니다.
> * redis를 이용하여 조회수를 따로 관리하고 조회수에 따라 티켓의 랭킹 부여 가능

---

## ❓ Trouble Shooting

### 최적화
- 문제상황 : 인덱스 적용 시 굉장히 미미한 수치로 속도개선이 됨.(약 1 sec)
- 해결방안 : ticketKeyword로 제목과 내용을 한번에 검색하였으나, ticketTitle, ticketContents로 제목과 내용을 다른 매개변수로 받아 검색 (약 50% 가까운 조회속도 개선 효과 발생!)
### 알맞은 담당자 찾기
- 문제상황 : 한 유저가 여러 담당자를 맡고 있어서 담당자를 로드할때 많은 값이 나오는 오류
- 해결방안 : 쿼리문을 작성하여 해당 워크스페이스와 유저의 아이디가 일치하는 담당자를 불러오도록 설계
### 낙관적 락 예외처리
- 문제상황 : 낙관적 락 테스트를 실행할 때, 고정된 문자데이터로만 수정되도록 설정하여 모수에 상관없이 항상 9개의 예외처리가 발생
- 해결방안 : 항상 랜덤한 새로운 문자데이터를 보내도록 UUID를 수정요청하는 데이터에 덧붙여 해결 
### QueryDSL에서 workspace.id 접근 문제
- 문제상황: 티켓을 workspace.id 로 필하터기 위해 각 테켓의 workspace.id 를 ticket.board.kanban.workspace.id 접근하려 했으나 member와 workspace가 연관관계가 형성되어서, ticket에
서 workspace를 근접할 수 없었습니다.
- 해결방안: ticket.member.workspace.id로 접근하여 문제를 해결 했습니다.
### redis 데이터 중복 문제
- 문제상황 : 조회수 데이터를 로드해서 다루는데 int타입뿐 아니라 String타입도 불러와져서 에러발생
- 해결방안 : 데이터 저장시 키값이 같아서 생긴 오류 -> 키값을 되도록 다르게 설정하여 저장하여 중복 발생 방지

---

## ✅ 5분 기록보드
![image](https://github.com/user-attachments/assets/1b8b02f8-c110-462d-8fd8-b9387638d823)


## 📝 Technologies & Tools 📝

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/> <img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white"/><img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"/> <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white"/> <img src="https://img.shields.io/badge/AmazonEC2-FF9900?style=for-the-badge&logo=AmazonEC2&logoColor=white"/> <img src="https://img.shields.io/badge/AmazonS3-569A31?style=for-the-badge&logo=AmazonS3&logoColor=white"/><img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"/> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"/>  <img src="https://img.shields.io/badge/GithubActions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white"/>  <img src="https://img.shields.io/badge/IntelliJIDEA-000000?style=for-the-badge&logo=IntelliJIDEA&logoColor=white"/>  <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white"/> <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white"/> <img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white"/>  <img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white"/>
