# 🎓 E-Learning Backend – Formation & Event Management Platform

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Live Demo](https://img.shields.io/badge/Live%20Demo-Swagger%20UI-success)](https://e-learningapp-5dxn.onrender.com/swagger-ui/index.html)

A **production-style backend system** for a coding school where students enroll in **formations (multi-course programs)** or **events**. The platform manages instructors, payments, and secure access to content using **time-based business rules**.

> ✅ **Live Swagger UI**: [https://e-learningapp-5dxn.onrender.com/swagger-ui/index.html](https://e-learningapp-5dxn.onrender.com/swagger-ui/index.html)
> *(Free tier may cold start – wait a few seconds if a 503 appears.)*

---

## ⭐ Key Highlights

* 🔐 Secure authentication using **JWT + Refresh Token rotation**
* 📧 **Email verification flow** before account activation (secure onboarding)
* ⏱️ **Automated access control** based on payment validity (scheduled jobs)
* 🧠 Clean architecture using **DTOs, validation, and global exception handling**
* ☁️ Media management with **Cloudinary integration**
* 🐳 Fully **Dockerized environment** (API + PostgreSQL)
* 🧪 Tested with **JUnit (unit & integration)** + Postman
* 📄 Fully documented API with **Swagger (OpenAPI)**

---

## 🏗️ Architecture

The application follows a **layered architecture**:

* **Controller Layer** → Handles HTTP requests and responses
* **Service Layer** → Contains business logic
* **Repository Layer** → Manages database interaction (JPA/Hibernate)
* **DTO Layer** → Ensures secure and controlled data transfer
* **Security Layer** → JWT-based authentication and authorization
* **Exception Handling** → Centralized via `GlobalExceptionHandler`

This design ensures **scalability, maintainability, and separation of concerns**.

---

## 🚀 Core Features

### 👨‍🏫 Manager

* Full CRUD for formations, courses, events, users, payments
* Assign instructors to formations (minimum one required)
* Manage student enrollments and payments
* Upload media (videos, PDFs, images) via Cloudinary

### 👨‍🎓 Student

* Register and manage profile (validated inputs)
* 📧 Verify email before account activation
* Enroll in formations (requires active payment)
* Enroll in events
* Access course content only when payment is valid

### 👨‍🏫 Instructor

* View assigned formations and courses
* Update profile and availability

---

## 📧 Email Verification Flow

* Upon registration, users receive a **verification email** containing a unique token
* The account remains **inactive until email is verified**
* Verification endpoint activates the account securely
* Prevents fake accounts and strengthens authentication flow

👉 This simulates real-world **secure onboarding practices** used in production systems

---

## 🔁 Automated Access Control (Key Business Logic)

* Each **Enrollment** is linked to **Payment records**
* A scheduled job runs daily:

  * If last payment ≤ 30 days → `PAID`
  * Otherwise → `NOT_PAID`
* Access to course content is **strictly controlled** by this status

👉 This simulates a real-world **subscription-based access system**

---

## 🔐 Security

* Stateless authentication using **JWT (access + refresh tokens)**
* Role-based authorization: `MANAGER`, `STUDENT`, `FORMATEUR`
* Secure endpoints via Spring Security filters
* Token expiration + refresh flow implemented
* Email verification required before login access

---

## ✅ Validation & Error Handling

* Input validation using `@Valid`, `@NotNull`, `@Pattern`, and custom annotations
* Centralized error handling via `GlobalExceptionHandler`
* Structured error responses

**Example:**

```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "phoneNumber": "Invalid format",
    "password": "Weak password"
  }
}
```

---

## 🛠️ Tech Stack

* **Spring Boot** – REST API development
* **Spring Security + JWT** – Authentication & Authorization
* **Spring Data JPA (Hibernate)** – ORM
* **PostgreSQL** – Relational Database
* **Docker & Docker Compose** – Containerization
* **Cloudinary** – Media storage
* **Swagger (OpenAPI)** – API documentation
* **JUnit 5 & Mockito** – Testing
* **Postman** – API testing

---

## ⚙️ Setup & Run

### 🐳 Docker (Recommended)

```bash
git clone https://github.com/your-username/E-LearningAPP.git
cd E-LearningAPP
docker-compose up --build
```

Access:

* API → [http://localhost:8080](http://localhost:8080)
* Swagger → [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🔑 Environment Variables

```properties
POSTGRES_DB=elearning_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_password

JWT_SECRET=your_secret
JWT_REFRESH_SECRET=your_refresh_secret

CLOUDINARY_CLOUD_NAME=your_cloud
CLOUDINARY_API_KEY=your_key
CLOUDINARY_API_SECRET=your_secret
```

> ⚠️ Never commit secrets. Use `.env` files or environment configs.

---

## 📡 API Documentation

All endpoints are available via Swagger:
👉 [https://e-learningapp-5dxn.onrender.com/swagger-ui/index.html](https://e-learningapp-5dxn.onrender.com/swagger-ui/index.html)

---

## 🧪 Testing

```bash
mvn test
```

* Unit & integration tests implemented
* Manual testing via Postman

---

## 🚢 Deployment

* Deployed on **Render (free tier)**
* May experience cold starts after inactivity

---

## 📌 Future Improvements

* Payment gateway integration (Stripe)
* Email notifications & reminders
* Advanced filtering/search
* Feedback & rating system
*Ci/Cd pipeline
---

## 👤 Author

Ahmed Yassine
Backend Developer 

---

## 📝 License

MIT License
