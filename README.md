# 🎓 Formation & Event Management Platform - Backend API

This is a **backend system** for managing a Training Center (`Centre de Formation`) where **students can enroll in formations (multi-course programs) or events**. Managers assign instructors (formateurs) to courses, and the system provides full tracking of participation and feedback.

> 🛡️ Includes robust **JWT-based authentication**, full **CRUD support**, **Dockerized PostgreSQL**, **Cloudinary integration**, and insightful **ETL dashboards via Power BI**.

---

## 🚀 Key Features

### 👨‍🎓 Student Module:
- Register and update personal info
- Enroll in **formations** (programs with multiple courses)
- Sign up for **events**

### 👨‍🏫 Manager Module:
- Manage formations, courses, and events
- Assign courses to formateurs
- View ratings and participation metrics

### ⚙️ Full CRUD Operations
All core entities (Students, Formations, Courses, Events, Managers,Enrollement) support:
- **Create**
- **Read**
- **Update**
- **Delete**

### 🔐 JWT Security (JSON Web Token)

- Implements **stateless authentication** using JWT
- Each request must include a valid token in headers
- Access is controlled by **user roles** (e.g., student, manager, admin)
- Token contains user identity and role info for secured endpoints
- Expiration and refresh logic ensures optimal security and performance
- **Efficient filtering and exception handling** to prevent unauthorized access

> 🛡️ Your app is protected by token-based security that scales — no sessions, just signed tokens and smart middleware.

![image](https://github.com/user-attachments/assets/73491dcd-1707-427c-8e4e-091c0588a5f7)

---

## 📊 Power BI Dashboard Highlights

- 📈 **Average Student Age** per event/formation  
- 📆 **Engagement Trend** per month  
- 🎟️ **Attendance tracking** per event  
- ⭐ **Rating visualizations** for events and formations
![image](https://github.com/user-attachments/assets/2a0b2c68-1158-4270-b1fc-e2d118fe3168)
![image](https://github.com/user-attachments/assets/b0da8a45-c3a8-476d-9709-a8bebd13ad87)

> All metrics are updated using Power BI's ETL pipeline that queries your PostgreSQL database.

---

## 🛠️ Tech Stack

| Tool/Tech         | Purpose                              |
|-------------------|--------------------------------------|
| **Spring Boot**   | Backend API + Security (JWT)         |
| **PostgreSQL**    | Relational Database (Dockerized)     |
| **Cloudinary**    | Store student/course images          |
| **Power BI**      | ETL & Dashboard                      |
| **Docker**        | Running PostgreSQL locally           |
| **JPA (Hibernate)** | ORM to manage entities             | 
|**Postman**        |  Manual API testing and debugging    |
|**Swager**         | API documentation and interactive testing UI|


---

## 🐳 PostgreSQL via Docker

Launch PostgreSQL with Docker:

docker-compose -f docker/postgres-compose.yml up -d



🧪 Running the Project
Follow the steps below to get the project up and running locally.


📦 1. Clone the Repository
git clone https://github.com/your-username/your-repo-name.git
cd your-repo-name

⚙️ 2. Configure the Application
Open the file:
src/main/resources/application.properties

And update the following according to your environment 

🐳 3. Start PostgreSQL with Docke
docker run --name training-db \
    -e POSTGRES_DB=your_db \
    -e POSTGRES_USER=your_user \
    -e POSTGRES_PASSWORD=your_password \
    -p 5432:5432 \
    -d postgres
  🔐 Ensure the DB credentials match what's in application.properties.
  
  📌 Future Improvements
Add email notifications on event registration

Enable search & filter in formations

Add comments/feedback system linked to Power BI

Deploy backend to a cloud server (Heroku / AWS / Render)

👤 Author
Ahmed Yassine
🎓 Final-year Business Intelligence student
⚙️ Backend Developer | BI Enthusiast

📝 License
This project is licensed under the MIT License.
# E-LearningAPP
# E-LearningAPP
