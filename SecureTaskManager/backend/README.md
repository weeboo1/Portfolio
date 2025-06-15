# SecureTaskManager

## Project Overview

SecureTaskManager is a backend REST API built with Spring Boot for secure task management. It supports user registration, JWT-based authentication with refresh tokens, role-based access, task CRUD, brute-force protection, and audit logging. Swagger UI is included for API documentation.

---

## Features

- User registration and login with encrypted passwords
- JWT access and refresh tokens
- Role-based access control (only one role implemented yet)
- Task management (create, read, update, delete)
- Brute-force login protection
- Audit logging of key user actions
- API documentation with Swagger UI
- Input validation
- PostgreSQL database persistence

---

## Technologies

- Java 21
- Spring Boot 3.2.5
- Spring Security
- JWT (io.jsonwebtoken)
- Spring Data JPA
- PostgreSQL
- Swagger (springdoc-openapi)
- Hibernate Validator
- Lombok
- Maven

---

## How to Run

1. Clone the repository and navigate to the backend folder.
2. Configure your PostgreSQL database and update `src/main/resources/application.yml` with your connection details.
3. Build the project using Maven: mvn clean package
4. Run the application:
5. Open Swagger UI at:
http://localhost:8080/swagger-ui.html
to explore and test the API.

---
By Halahan Kiril
