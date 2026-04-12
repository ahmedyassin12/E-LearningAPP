# 🎓 E-Learning Backend – Formation & Event Management Platform

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-7-red.svg)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)](https://www.docker.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Live Demo](https://img.shields.io/badge/Live%20Demo-Swagger%20UI-success)](https://e-learningapp-5dxn.onrender.com/swagger-ui/index.html)
[![Tests](https://img.shields.io/badge/Tests-100%2B-brightgreen.svg)]()

A **production-style backend system** for a coding school where students enroll in **formations (multi-course programs)** or **events**. The platform manages instructors, payments, and secure access to content using **time-based business rules**, **distributed caching**, and **role-based authorization**.

> ✅ **Live Swagger UI**: [https://e-learningapp-5dxn.onrender.com/swagger-ui/index.html](https://e-learningapp-5dxn.onrender.com/swagger-ui/index.html)
> *(Free tier — may cold start after inactivity. Wait a few seconds if a 503 appears.)*

---

## ⭐ Key Highlights

- 🔐 Stateless authentication using **JWT access + refresh token rotation**
- 📧 **Email verification flow** before account activation (Brevo SMTP relay)
- ⚡ **Distributed Redis caching** for JWT blacklisting, formations, and courses
- ⏱️ **Automated subscription access control** via scheduled jobs
- 🧠 Clean layered architecture — DTOs, validators, global exception handling
- ☁️ Media management with **Cloudinary integration**
- 🐳 Fully **Dockerized** — single `docker-compose up` starts everything
- 🧪 **100+ tests** — JUnit 5 unit & integration tests + Postman collection
- 📄 Fully documented API via **Swagger (OpenAPI 3)**

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Client (Postman / Frontend)               │
└───────────────────────────────┬─────────────────────────────────┘
                                │ HTTP
┌───────────────────────────────▼─────────────────────────────────┐
│                    Controller Layer (@RestController)            │
│              Handles HTTP requests, delegates to services        │
└───────────────────────────────┬─────────────────────────────────┘
                                │
┌───────────────────────────────▼─────────────────────────────────┐
│                     Service Layer (@Service)                     │
│         Business logic, caching, scheduling, validation          │
└──────────────┬──────────────────────────────┬───────────────────┘
               │                              │
┌──────────────▼──────────┐      ┌────────────▼──────────────────┐
│  Repository Layer (JPA) │      │      Redis Cache Layer         │
│  PostgreSQL via Hibernate│      │  JWT tokens, formations,      │
│                         │      │  courses (Upstash in prod)     │
└─────────────────────────┘      └───────────────────────────────┘
               │
┌──────────────▼──────────┐
│     PostgreSQL Database  │
│   Users, Formations,     │
│   Enrollments, Payments  │
└─────────────────────────┘
```

### Layer Responsibilities

**Controller Layer** — Receives HTTP requests, validates input via `@Valid`, delegates to services, returns structured responses.

**Service Layer** — Contains all business logic: enrollment rules, payment validation, scheduled access control, cache management, and email dispatch.

**Repository Layer** — Spring Data JPA repositories with custom JPQL queries for complex filtering and role-based data access.

**Security Layer** — JWT filter chain with `OncePerRequestFilter`, role-based access (`MANAGER`, `STUDENT`, `FORMATEUR`), and Database-backed blacklist + Redis as a cache layer.

**Cache Layer** — Redis-backed distributed cache replacing in-memory Ehcache, enabling cache sharing across instances and persistence across restarts.

---

## 🚀 Core Features

### 👨‍💼 Manager
- Full CRUD for formations, courses, events, users, and payments
- Assign instructors to formations (minimum one required)
- Manage student enrollments and payment records
- Upload media (videos, PDFs, images) via Cloudinary

### 👨‍🎓 Student
- Register with validated inputs and strong password enforcement
- Email verification required before account activation
- Enroll in formations — access gated by payment validity
- Enroll in events and access course content when subscription is active

### 👨‍🏫 Instructor (Formateur)
- View only assigned formations and courses
- Update profile and availability status
- Role-scoped data access — cannot see other instructors' formations

---

## ⚡ Redis Caching Strategy

One of the core production features of this system is a **distributed Redis cache** (Upstash in production, Docker locally) that significantly reduces database load.

### What is Cached

| Cache Name | Content | TTL | Key Strategy |
|---|---|---|---|
| `JwtTokens` | JWT tokens | 7 days | `tokenString` |
| `studentFormation` | Per-student formation view with enrollment & payment status | 10 min | `userId_formationName` |
| `AllStudentFormations` | Full formation list per student | 10 min | `userId` |
| `GetCourses` | Course list per formation | 10 min | `formationId` |
| `GetCourse` | Single course details | 10 min | `courseId` |

### Why Per-User Cache Keys

Student formation DTOs include `isPaid` and `isEnrolled` — data that differs per student. Using only `formationId` as a key would serve one student's enrollment status to another. Keys are always composed as `userId + formationName` to guarantee isolation.

### Eviction Strategy

- **Formation updated** → `allEntries = true` on `studentFormation` and `AllStudentFormations` — all student views are stale
- **Student enrolls or pays** → evict by specific `userId_formationName` key only
- **User logs out** → JWT token evicted from `JwtTokens` cache immediately

### Cache Serialization

Uses `GenericJackson2JsonRedisSerializer` with a custom `ObjectMapper` configured with `JavaTimeModule` for `LocalDate` support. Specific caches (e.g. JWT) use `Jackson2JsonRedisSerializer<Token>` for type safety and smaller JSON footprint.

---

## 📧 Email Verification Flow

```
User registers
      ↓
Account created (disabled)
      ↓
Verification token generated (UUID, 30 min expiry)
      ↓
Email sent via Brevo SMTP relay
      ↓
User clicks link → /api/v1/verify?token=...
      ↓
Token validated → account activated ✅
```

Uses **Brevo** (formerly Sendinblue) as the SMTP relay in production — Gmail SMTP is blocked on Render's infrastructure. Local development uses Gmail SMTP directly.

---

## 🔁 Automated Access Control (Key Business Logic)

Each `Enrollment` is linked to `Payment` records. A **Spring `@Scheduled` job** runs daily:

- Last payment date ≤ 30 days → status = `PAID` → course access granted
- Last payment date > 30 days → status = `NOT_PAID` → access revoked

This simulates a real-world **subscription-based content access system** — similar to how platforms like Coursera or Udemy manage paid content access.

---

## 🔐 Security

- Stateless JWT authentication with **access token + refresh token rotation**
- Role-based authorization: `MANAGER`, `STUDENT`, `FORMATEUR`
- Database-backed blacklist + Redis as a cache layer — invalidated tokens rejected even before expiry
- `OncePerRequestFilter` intercepts all requests for token validation
- Email verification required after Account registration action
- Passwords validated with custom `@StrongPassword` annotation

---

## ✅ Validation & Error Handling

Custom validation layer using `ObjectValidator<T>` wrapping Jakarta Bean Validation — collects all violations and throws them as a structured set rather than failing on the first error.

Custom annotations implemented:
- `@StrongPassword` — enforces complexity rules
- `@PhoneNumber` — validates format specific for Tunisian Numbers
- `@UsernameValidator` — enforces username rules

Global exception handling via `@RestControllerAdvice`:

```json
{
  "errors": [
    "Password must contain at least one uppercase letter",
    "Phone number format is invalid"
  ]
}
```

---

## 🧪 Testing

100+ test cases covering service and repository layers.

| Layer | Framework | Coverage |
|---|---|---|
| Service Layer | JUnit 5 + Mockito | Unit tested |
| Repository Layer | JUnit 5 + `@DataJpaTest` | DAO queries tested |
| Integration | `@SpringBootTest` | End-to-end flows |
| API | Postman | All endpoints manually verified |

```bash
mvn test
```

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| **Spring Boot 3.1** | REST API framework |
| **Spring Security + JWT** | Authentication & authorization |
| **Spring Data JPA (Hibernate)** | ORM & database access |
| **PostgreSQL 15** | Relational database |
| **Redis 7 (Upstash)** | Distributed caching & JWT blacklisting |
| **Docker & Docker Compose** | Containerized local environment |
| **Cloudinary** | Media file storage (images, videos, PDFs) |
| **Brevo (SMTP relay)** | Transactional email delivery |
| **Swagger (OpenAPI 3)** | API documentation |
| **JUnit 5 + Mockito** | Unit & integration testing |

---

## 💡 Design Decisions

**Why Redis over Ehcache?**
Ehcache is in-memory and instance-bound — it cannot be shared across multiple app instances and is lost on restart. Redis is an external distributed cache that persists across restarts and scales horizontally. For a production system this is non-negotiable.

**Why JWT over Sessions?**
Stateless JWT authentication scales horizontally without shared session storage. Each request is self-contained — no session lookup required. Combined with Redis token blacklisting, we get the security benefits of session invalidation without the statefulness.

**Why Brevo over Gmail SMTP in production?**
Render's infrastructure blocks outbound SMTP ports 587 and 465 to prevent spam abuse. Brevo is a dedicated transactional email relay that operates over unrestricted ports. Gmail SMTP is kept for local development only.

**Why per-user cache keys for student formations?**
Student formation DTOs contain `isPaid` and `isEnrolled` — fields that differ per student. A shared cache key would serve incorrect payment/enrollment data across users. Keys are always scoped as `userId_formationName` to guarantee data isolation.

**Why a custom `ObjectValidator<T>` instead of `@Valid` directly?**
Spring's `@Valid` fails on the first constraint violation. The custom validator collects **all** violations and returns them together — better UX for API consumers who need to fix multiple fields at once.

**Why `@DiscriminatorValue` for user roles?**
`MANAGER`, `STUDENT`, and `FORMATEUR` share a `User` base entity via JPA single-table inheritance. This avoids redundant authentication logic across roles while keeping role-specific fields (e.g. `Formateur.skills`, `Formateur.availability`) cleanly separated.

---

## ⚙️ Setup & Run

### 🐳 Docker (Recommended)

```bash
git clone https://github.com/ahmedyassin12/E-LearningAPP.git
cd E-LearningAPP
docker-compose up --build
```

This starts:
- Spring Boot API on `http://localhost:8080`
- PostgreSQL on port `5432`
- Redis on port `6379`

Access Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### ▶️ Manual Run

```bash
mvn clean install
mvn spring-boot:run
```

---

## 🔑 Environment Variables

```properties
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/centre_Formation
DB_POSTGRESDB_USER=postgres
DB_POSTGRESDB_PASSWORD=your_password
spring.sql.init.platform=postgresql
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=true
#redis
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.cache.type=redis

# Email 
spring.mail.protocol=smtp
spring.mail.host=smtp-relay.brevo.com
spring.mail.port=587
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.connectiontimeout=10000
spring.mail.properties.mail.smtp.from={put your email here}
spring.mail.properties.mail.smtp.timeout=10000  
spring.mail.properties.mail.smtp.writetimeout=10000
# Cloudinary
add environment variables in CloudinaryService class "package com.example.demo.service.CloudinaryService"

# Redis (Upstash - production)
#redis
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.cache.type=redis
```

> ⚠️ Never commit secrets. Use `.env` files locally and platform environment variables in production.

---

## 📡 API Documentation

All endpoints documented and testable via Swagger:

👉 [https://e-learningapp-5dxn.onrender.com/swagger-ui/index.html](https://e-learningapp-5dxn.onrender.com/swagger-ui/index.html)

---

## 🚢 Deployment

- API deployed on **Render (free tier)**
- Database on **Neon PostgreSQL**
- Redis on **Upstash (free tier — no expiry)**
- Media on **Cloudinary**
- Email via **Brevo SMTP relay**

> Cold starts may occur after inactivity on the free tier.

---

## 📌 Future Improvements

- CI/CD pipeline with GitHub Actions (build → test → deploy)
- Payment gateway integration (Stripe)
- Email notifications & reminders for payment expiry
- Advanced filtering and search across formations
- Prometheus + Grafana monitoring dashboard

---

## 👤 Author

**Ahmed Yassine Zouaoui**
Backend Developer — Java / Spring Boot

---

## 📝 License

MIT License
