# 🎓 E-Learning Backend – Formation & Event Management Platform

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-7-red.svg)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)](https://www.docker.com/)
[![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-2088FF.svg?logo=github-actions&logoColor=white)](https://github.com/ahmedyassin12/E-LearningAPP/actions)
[![Tests](https://img.shields.io/badge/Tests-100%2B-brightgreen.svg)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Live Demo](https://img.shields.io/badge/Live%20Demo-Swagger%20UI-success)](https://e-learningapp-5dxn.onrender.com/swagger-ui/index.html)

A **production backend** for a coding school where students enroll in **formations (multi-course programs)** or **events**. The platform manages instructors, payments, and secure access to content using **time-based business rules**, **distributed caching**, **role-based authorization**, and an **automated CI/CD pipeline**.

> ✅ **Live Swagger UI**: [https://e-learningapp-5dxn.onrender.com/swagger-ui/index.html](https://e-learningapp-5dxn.onrender.com/swagger-ui/index.html)
> *(Free tier — may cold start after inactivity. Wait a few seconds if a 503 appears.)*

---

## ⭐ Key Highlights

- 🔐 Stateless authentication using **JWT access + refresh token rotation**
- 📧 **Email verification flow** before account activation (Brevo SMTP relay)
- ⚡ **Redis performance layer** — distributed caching for formations, courses, and validated sessions, with eviction-based invalidation backed by PostgreSQL as the source of truth
- ⏱️ **Automated subscription access control** — a daily scheduled job revokes course access the moment a payment lapses, with zero manual intervention
- 🔄 **CI/CD with GitHub Actions** — every push runs the full test suite and builds the Docker image before anything reaches production
- 🧠 Clean layered architecture — DTOs, custom validators, global exception handling
- ☁️ Media management via **Cloudinary**
- 🐳 Fully **Dockerized** — a single `docker-compose up` starts everything
- 🧪 **100+ tests** — JUnit 5 unit & integration tests + a full Postman collection
- 📄 Fully documented API via **Swagger (OpenAPI 3)**

---

## 🔄 CI/CD Pipeline (GitHub Actions)

Every push and pull request triggers an automated workflow that guarantees nothing untested reaches production:

```
Push / PR
    ↓
Checkout + set up JDK 17
    ↓
mvn clean verify  → runs 100+ unit & integration tests
    ↓
Build Docker image
    ↓
(on main) Deploy trigger → Render
```

- **Build & test** on every push and PR — a red pipeline blocks the merge.
- **Containerized build** ensures the image that passes CI is the image that ships.
- **Branch-scoped deploys** — only `main` triggers the production deploy step.

This replaces "it works on my machine" with a reproducible, gated path to production.

---

## 🏗️ Architecture

```
┌──────────────────────────────────────────────────────────────┐
│                 Client (Postman / Frontend)                  │
└───────────────────────────────┬──────────────────────────────┘
                                │ HTTP (JWT in Authorization header)
┌───────────────────────────────▼──────────────────────────────┐
│             Security Filter (OncePerRequestFilter)           │
│        Validates JWT → 1. Check Redis  2. On miss, check DB   │
└───────────────────────────────┬──────────────────────────────┘
                                │
┌───────────────────────────────▼──────────────────────────────┐
│                    Service Layer (@Service)                  │
│       Business logic · @Cacheable · token revocation         │
└──────────────┬───────────────────────────────┬───────────────┘
               │                               │
┌──────────────▼───────────┐      ┌────────────▼───────────────┐
│   Repository Layer (JPA) │      │       Redis Cache Layer     │
│   PostgreSQL via Hibernate│◄────►│   Eviction-based invalidation│
│  (Token, Formation, Course)│     │  Formations · Courses · Tokens│
└──────────────┬───────────┘      └────────────┬───────────────┘
               │                               │
┌──────────────▼───────────┐      ┌────────────▼───────────────┐
│    PostgreSQL Database    │      │        Redis Server         │
│    The "Source of Truth"  │      │   Performance Accelerator   │
│                           │      │ (on miss: fetch & re-sync)  │
└───────────────────────────┘      └─────────────────────────────┘
```

### Layer Responsibilities

**Controller Layer** — Receives HTTP requests, delegates to services, returns structured responses.

**Service Layer** — Contains all business logic: enrollment rules, payment validation, scheduled access control, cache management, and email dispatch. Input validation uses a custom `ObjectValidator<T>` (wrapping Jakarta Bean Validation) that collects and returns **all** constraint violations at once for a better API consumer experience.

**Repository Layer** — Spring Data JPA repositories with custom JPQL queries for complex filtering and role-based data access.

**Security Layer** — Implements a **durable-first** validation flow via `OncePerRequestFilter`. PostgreSQL is the absolute source of truth for token validity, ensuring maximum consistency; Redis acts as a distributed performance accelerator that caches validated sessions for sub-millisecond authentication. Any security state change (logout, new authentication, or refresh) triggers an immediate cache eviction, forcing the next request to re-verify against the database and preventing "ghost sessions."

**Cache Layer** — Redis-backed distributed cache replacing in-memory Ehcache, enabling cache sharing across instances and persistence across restarts.

---

## 🚀 Core Features

### 👨‍💼 Manager
- Full CRUD for formations, courses, events, users, and payments
- Assign instructors to formations (minimum one required)
- Manage student enrollments and payment records
- Upload media (videos, PDFs, images) via Cloudinary

### 👨‍🎓 Student
- Register with validated inputs and strong-password enforcement
- Email verification required before account activation
- Enroll in formations — access gated by payment validity
- Enroll in events and access course content while the subscription is active

### 👨‍🏫 Instructor (Formateur)
- View only assigned formations and courses
- Update profile and availability status
- Role-scoped data access — cannot see other instructors' formations

---

## ⚡ Redis Caching Strategy

A core production feature is a **distributed Redis cache** (Upstash in production, Docker locally) that significantly reduces database load.

### What is Cached

| Cache Name | Content | TTL | Key Strategy |
|---|---|---|---|
| `JwtTokens` | Validated JWT sessions | 7 days | `tokenString` |
| `studentFormation` | Per-student formation view with enrollment & payment status | 10 min | `userId_formationName` |
| `AllStudentFormations` | Full formation list per student | 10 min | `userId` |
| `GetCourses` | Course list per formation | 10 min | `formationId` |
| `GetCourse` | Single course details | 10 min | `courseId` |

### Why Per-User Cache Keys

Student formation DTOs include `isPaid` and `isEnrolled` — data that differs per student. Using only `formationId` as a key would serve one student's enrollment status to another. Keys are always composed as `userId + formationName` to guarantee isolation.

### Eviction Strategy

- **Formation updated** → `allEntries = true` on `studentFormation` and `AllStudentFormations` (all student views become stale)
- **Student enrolls or pays** → evict the specific `userId_formationName` key only
- **User logs out** → JWT session evicted from `JwtTokens` immediately

### Cache Serialization

Uses `GenericJackson2JsonRedisSerializer` with a custom `ObjectMapper` configured with `JavaTimeModule` for `LocalDate` support. Specific caches (e.g. JWT) use `Jackson2JsonRedisSerializer<Token>` for type safety and a smaller JSON footprint.

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

Production uses **Brevo** (formerly Sendinblue) as the SMTP relay because Gmail SMTP ports are blocked on Render's infrastructure. Local development uses Gmail SMTP directly.

---

## 🔁 Automated Access Control (Key Business Logic)

Each `Enrollment` is linked to `Payment` records. A **Spring `@Scheduled` job** runs daily:

- Last payment date ≤ 30 days → status = `PAID` → course access **granted**
- Last payment date > 30 days → status = `NOT_PAID` → access **revoked**

This simulates a real-world **subscription-based content access system** — similar to how platforms like Coursera or Udemy gate paid content — with no manual intervention required.

---

## 🔐 Security

- **Stateless JWT** — short-lived access token (2 min) + refresh token rotation
- **Zero-trust validation** — PostgreSQL is the absolute source of truth for token status; `OncePerRequestFilter` validates every request
- **Cache-aside acceleration** — valid sessions are cached in Redis to minimize DB overhead
- **Security via eviction** — logout, authentication, or refresh events trigger immediate cache eviction, forcing re-verification against the database
- **Account integrity** — email verification required after registration
- **Role-Based Access Control (RBAC)** — strict permission scoping for `MANAGER`, `STUDENT`, and `FORMATEUR` via Spring Security method-level protection

---

## ✅ Validation & Error Handling

A custom validation layer using `ObjectValidator<T>` wraps Jakarta Bean Validation — it collects **all** violations and returns them as a structured set rather than failing on the first error.

Custom annotations:

- `@StrongPassword` — enforces complexity rules
- `@PhoneNumber` — validates Tunisian phone number format
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

100+ test cases covering the service and repository layers.

| Layer | Framework | Coverage |
|---|---|---|
| Service Layer | JUnit 5 + Mockito | Unit tested |
| Repository Layer | JUnit 5 + `@DataJpaTest` | DAO queries tested |
| Integration | `@SpringBootTest` | End-to-end flows |
| API | Postman | All endpoints verified |

```bash
mvn test
```

The full suite runs automatically on every push via the GitHub Actions pipeline.

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| **Spring Boot 3.1** | REST API framework |
| **Spring Security + JWT** | Authentication & authorization |
| **Spring Data JPA (Hibernate)** | ORM & database access |
| **PostgreSQL 15** | Relational database |
| **Redis 7 (Upstash)** | Distributed caching |
| **Docker & Docker Compose** | Containerized environment |
| **GitHub Actions** | CI/CD — build, test, deploy |
| **Cloudinary** | Media storage (images, videos, PDFs) |
| **Brevo (SMTP relay)** | Transactional email delivery |
| **Swagger (OpenAPI 3)** | API documentation |
| **JUnit 5 + Mockito** | Unit & integration testing |

---

## 💡 Design Decisions

**Why Redis over Ehcache?**
Ehcache is in-memory and instance-bound — it can't be shared across instances and is lost on restart. Redis is an external distributed cache that persists across restarts and scales horizontally. For a production system this is non-negotiable.

**Why JWT over sessions?**
Stateless JWT scales horizontally without shared session storage — each request is self-contained. Combined with database-backed token validation and Redis eviction, it delivers the security of immediate session invalidation ("killing" tokens on demand) without sacrificing the scalability of a stateless architecture.

**Why Brevo over Gmail SMTP in production?**
Render blocks outbound SMTP ports 587 and 465 to prevent spam abuse. Brevo is a dedicated transactional email relay operating over unrestricted ports. Gmail SMTP is kept for local development only.

**Why per-user cache keys for student formations?**
Student formation DTOs contain `isPaid` and `isEnrolled` — fields that differ per student. A shared cache key would serve incorrect payment/enrollment data across users. Keys are always scoped as `userId_formationName` to guarantee isolation.

**Why a custom `ObjectValidator<T>` instead of `@Valid` directly?**
Spring's `@Valid` fails on the first constraint violation. The custom validator collects **all** violations and returns them together — better UX for API consumers who need to fix multiple fields at once.

**Why `@DiscriminatorValue` for user roles?**
`MANAGER`, `STUDENT`, and `FORMATEUR` share a `User` base entity via JPA single-table inheritance. This avoids redundant authentication logic across roles while keeping role-specific fields (e.g. `Formateur.skills`, `Formateur.availability`) cleanly separated.

---

## ⚙️ Setup & Run

### 🔑 Environment Variables

```properties
# --- Application Profile (Options: Local, Prod) ---
SPRING_PROFILES_ACTIVE=Local

# --- Database (PostgreSQL) ---
DB_URL=jdbc:postgresql://localhost:5432/elearning
DB_USERNAME=your_db_user
DB_PASSWORD=your_db_password

# --- Email Service (Brevo / SMTP) ---
BREVO_USERNAME=your_brevo_username
BREVO_PASSWORD=your_brevo_password
BREVO_SENDER=your_email@gmail.com

# --- Cloudinary (Media Storage) ---
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_cloud_key
CLOUDINARY_API_SECRET=your_cloud_secret

# --- Redis ---
REDIS_HOST=localhost
REDIS_PORT=6379
```

> ⚠️ Never commit secrets. Use `.env` files locally and platform environment variables in production.

### 🐳 Docker (Recommended)

```bash
git clone https://github.com/ahmedyassin12/E-LearningAPP.git
cd E-LearningAPP
docker-compose up --build
```

This starts:

- Spring Boot API on `http://localhost:8088`
- PostgreSQL on port `5432`
- Redis on port `6379`

Access Swagger at: [http://localhost:8088/swagger-ui/index.html](http://localhost:8088/swagger-ui/index.html)

### ▶️ Manual Run

```bash
mvn clean install
mvn spring-boot:run
```

---

## 📡 API Documentation

All endpoints are documented and testable via Swagger:
👉 [https://e-learningapp-5dxn.onrender.com/swagger-ui/index.html](https://e-learningapp-5dxn.onrender.com/swagger-ui/index.html)

---

## 🚢 Deployment

- API deployed on **Render** (free tier)
- Database on **Neon PostgreSQL**
- Redis on **Upstash** (free tier — no expiry)
- Media on **Cloudinary**
- Email via **Brevo SMTP relay**
- Build, test & deploy automated via **GitHub Actions**

> Cold starts may occur after inactivity on the free tier.

---

## 📌 Future Improvements

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
