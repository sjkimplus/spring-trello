# SPRING-TRELLO

## 👋🏻 Introduction

---
Spring-Trello is a clone coding project of the popular productivity app Trello using springboot and java. <br>
This README includes the following table of contents:

1. Tech Stack
2. Our Team
3. Wireframe
4. Entity Relationship Diagram (ERD)
5. Key Project Features
6. API Documentation
7. Trouble Shooting

## 🛠️ Tech Stack

---
<div>
<h3>Language & Framework</h3>
      <div>
        <img src="https://img.shields.io/badge/java-1E8CBE.svg?style=for-the-badge&logo=java&logoColor=white">
        <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white"/>
        <img src="https://img.shields.io/badge/jwt-000000?style=for-the-badge&logo=json web tokens&logoColor=white" alt="jwt Badge">
        <img src="https://img.shields.io/badge/jpa-527FFF?style=for-the-badge&logo=jpa&logoColor=white" alt="jpa Badge">
        <br>
        <img src="https://img.shields.io/badge/spring boot-%236DB33F.svg?style=for-the-badge&logo=spring boot&logoColor=white">
        <img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=spring security&logoColor=white" alt="spring security Badge">
      </div>
<h3>Database</h3>
      <div>
        <img src="https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white">
        <img src="https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white">
      </div>
<h3>CI / CD</h3>
      <div>
        <img src="https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white">
      </div>
<h3>Cloud</h3>
      <div>
        <img src="https://img.shields.io/badge/ec2-FF9900?style=for-the-badge&logo=amazon ec2&logoColor=white" alt="EC2 Badge">
        <img src="https://img.shields.io/badge/s3-569A31?style=for-the-badge&logo=amazon s3&logoColor=white" alt="S3 Badge">
      </div>
<h3>Testing</h3>
      <div>
        <img src="https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white" alt="junit5 Badge">
        <img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white" alt="postman Badge">
      </div>
<h3>Others</h3>
    <div>
        <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"/>
        <img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white"/>
        <img src="https://img.shields.io/badge/IntelliJIDEA-000000?style=for-the-badge&logo=IntelliJIDEA&logoColor=white"/> 
        <img src="https://img.shields.io/badge/Notion-white?style=for-the-badge&logo=Notion&logoColor=black"/>
        <img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white"/>
    </div>
</div>




## 👨‍👩‍👧‍👦 Our Team

---

| Name           | Domain in Charge  | Blog Link                                        | GitHub Link                               |
|----------------|-------------------|--------------------------------------------------|-------------------------------------------|
| Sungju Kim     | User              | [Blog Link](https://velog.io/@sjkimplus09/posts) | [GitHub](https://github.com/sjkimplus)    |
| Daehyeon Park  | Board, Ticket     | [Blog Link](https://velog.io/@pdh6113)           | [GitHub](https://github.com/pdhpark)      |
| Chaegyu Im     | Member, Kaanban   | [Blog Link](https://checkuu.tistory.com/)        | [GitHub](https://github.com/checkuu-0216) |
| Jinho Jeong    | Workspace, Comment | [Blog Link](https://xabierj.tistory.com/)        | [GitHub](https://github.com/XabierJEONG)  |

## 🖋️ Wireframe

---
![image](https://github.com/user-attachments/assets/6c8e9fb1-df1c-4afc-8c54-44f431346d58)


## 📋 ERD

---
![image](https://github.com/user-attachments/assets/a565d799-53d6-4347-bde5-a607650a6580)


## ⭐ Key Project Features

---
### 🗨 Kanban Board Collaboration Tool (CRUD)
> * Members within a workspace can organize and share tasks.
>   1. Workspace Create/Update/Delete: After creating a workspace, colleagues can be invited to collaborate.
>   2. Ticket and Comment Create/Update/Delete/View: File attachments can be added, and managers can be assigned to tasks.
>   3. View Count: Records ticket views, enabling daily rankings based on view count.

### 🔔 Slack Notifications
> * Notifications are sent to Slack for specific events using the Slack API.
> * For example, when a new member is added to a workspace, a notification is sent regarding which workspace the member has been registered to.
> * Also, when a new comment is added to a ticket, it notifies which ticket received the comment.

### 📁 File Attachments
> * Implemented file attachment registration, modification, and deletion feature for tickets using AMAZON S3.
> * Designed an independent entity class with relationships (many to one) to enable multiple file attachments per ticket.

### 🕙 Concurrency Control
> * Prevents data inconsistencies when multiple users simultaneously edit the same ticket using locks.
> * Compared the performance and consistency guarantees of optimistic and pessimistic locks via test codes.

### ⌛ Caching 
> * Used with Redis caching to prevent DB bottlenecks during repeated requests.
> * View counts are managed separately using Redis caching, enabling efficienty popularity ranking based on view counts.
> * View counts are reset every 24 hours, and this is done via Redis' TimeToLive(TTL) feature.


### 🔍 Search Functionality (Content, Title, Deadline, Assignee, Board ID)
> * Implemented dynamic query generation using QueryDSL.
> * Allows searching within the workspace the current user is part of, based on the ticket title, content, deadline, assignee, or board ID.




##  📝 API Documentation

---

### 1️⃣ Sign-Up/Login Functionality & TICKET Search Functionality (Lead: Kim Sungju)
![image](https://github.com/user-attachments/assets/035a73ad-cabd-4542-a26c-8654351401e7)

### 2️⃣ MEMBER & KANBAN Functionality (Lead: Lim Chaegyu)
![image](https://github.com/user-attachments/assets/d07a4627-0601-41be-8ce3-274ebe8b9fee)

### 3️⃣ WORKSPACE & COMMENT Functionality (Lead: Jung Jinho)
![image](https://github.com/user-attachments/assets/e79b3d2b-03f0-4aec-a625-062a0866ae04)

### 4️⃣ BOARD & TICKET Functionality (Lead: Park Daehyun)
![image](https://github.com/user-attachments/assets/e04b7fd1-7458-40fd-a952-7eb30734c17c)
![image](https://github.com/user-attachments/assets/474f1826-d6ce-48c7-8ba3-d473595ab6e4)
![image](https://github.com/user-attachments/assets/06cdae45-329b-47cf-b583-0dbea5c75417)



## 🧹 Troubleshooting

---

### Search Optimization via Indexing
- **Problem:** Applying an index resulted in negligible performance improvement (approx. 1 second).
- **Solution:** Initially searched both title and content with `ticketKeyword`, but separated them into `ticketTitle` and `ticketContents` for individual queries, achieving nearly a 50% query speed improvement.

### Optimistic Lock Exception Handling
- **Problem:** While testing optimistic locks, modifying with fixed string data always caused exceptions, regardless of sample size.
- **Solution:** Appended random unique string data (UUID) to requests for modification, resolving the issue.

### QueryDSL Workspace ID Access Issue
- **Problem:** Attempting to filter tickets by `workspace.id` required accessing `ticket.board.kanban.workspace.id`. However, since `member` and `workspace` were related, direct access from `ticket` to `workspace` was not possible.
- **Solution:** Resolved the issue by accessing via `ticket.member.workspace.id`.

### Redis Data Duplication Issue
- **Problem:** Errors occurred while loading view count data due to mixed types (int and String).
- **Solution:** The error stemmed from duplicate keys during data storage. Ensured unique key assignments to prevent duplication.
