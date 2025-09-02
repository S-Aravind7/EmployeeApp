# EmployeeApp

A Spring Boot application with **JWT-based Authentication & Authorization** where:

- **Admin** can perform full CRUD operations on Employees.
- **User** can only view the list of employees.
- **Public endpoints** for Register & Login.



## 🚀 Features
- User Registration & Login (JWT Authentication).
- Role-based Authorization (`ADMIN` vs `USER`).
- Employee Management:
  - Create Employee ✅ (Admin only)
  - Update Employee ✅ (Admin only)
  - Delete Employee ✅ (Admin only)
  - Get All Employees ✅ (User/Admin)
  - Get Employee By ID ✅ (Admin)
  - Get Employees By Department ✅ (Admin)
- **Readable Code** → Added comments throughout the project for better understanding.



## 🛠️ Tech Stack
- Java 17+
- Spring Boot 3
- Spring Security + JWT
- Hibernate / JPA
- H2 (in-memory) or MySQL (configurable)
- Maven



## ⚙️ Setup & Run

### 1. Clone the repository
```bash
git clone https://github.com/S-Aravind7/EmployeeApp.git
cd EmployeeApp
