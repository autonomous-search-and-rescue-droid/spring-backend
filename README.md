# Spring Backend For Autonomous Search And Rescue Droid

This repository contains the backend service for the Autonomous Search and Rescue Droid project. Built with Spring Boot, it provides REST APIs and backend logic to support the operations of the search and rescue system.  

## Table of Contents
- [Features](#features)  
- [Getting Started](#getting-started)  
- [API Documentation](#api-documentation)  
- [Project Structure](#project-structure)  
- [Contributing](#contributing)  
- [License](#license)  

## Features
- RESTful APIs for droid control and telemetry  
- Integration with search and rescue mission logic  
- Secure authentication and authorization using JWT  
- Role-based access control (Admin, User, Common)  
- Database support for mission and droid data  
- Configurable and extensible Spring Boot architecture  

## Getting Started

### Prerequisites
- Java 21  
- Maven 3.6+  
- Docker with Compose support  

### Setup

Clone the repository:  
```bash
git clone https://github.com/autonomous-search-and-rescue-droid/spring-backend.git  
cd spring-backend  
```

Create a local env file:
```bash
cp .env.example .env
```

Start the backend manually on Ubuntu:
```bash
docker-compose up -d mongodb
env SPRING_DOCKER_COMPOSE_ENABLED=false SPRING_DATA_MONGODB_URI='mongodb://root:secret@localhost:27017/search_droid_db?authSource=admin' ./mvnw -Dmaven.test.skip=true -Djava.version=17 -Dmaven.compiler.release=17 -Dmaven.compiler.fork=true -Dmaven.compiler.executable=/usr/bin/javac spring-boot:run
```

Or use the Makefile shortcut:
```bash
make backend
```

Access the API at: <http://localhost:8080>

The application loads `.env` automatically on startup. For Docker MongoDB, the default `.env.example` URI points to the Compose container with authentication enabled.

### MongoDB Notes

- The backend uses `SPRING_DATA_MONGODB_URI` when present.
- The included `compose.yaml` starts MongoDB on `localhost:27017`.
- `make backend` first runs `docker-compose up -d mongodb`, then starts Spring Boot with the Ubuntu-compatible compiler settings that worked in this environment.
- Roles are seeded automatically on startup for a fresh database, so signup works without manual inserts.

## API Documentation

This section provides details on the available API endpoints for interacting with the application.
[More detailed documentation available here](https://docs-jade-nu.vercel.app)

### Base URL

All API endpoints are prefixed with: `http://localhost:8080` (currently running in localhost).

---

### Authentication APIs

#### `POST /api/auth/signin`

Authenticate a user and return a JWT token.

##### Request Body:

```json
{
  "username": "exampleUser",
  "password": "examplePassword"
}
```

##### Response (success)
```json
{
  "token": "jwt-token",
  "id": 1,
  "username": "exampleUser",
  "email": "user@example.com",
  "roles": ["ROLE_USER"]
}
```

##### Response (If email not verified)
```json
{
  "message": "Email not verified. Please verify your email to sign in."
}
```


#### `POST /api/auth/signup`

Register a new user

##### Register a new user
```json
{
  "username": "newUser",
  "email": "newuser@example.com",
  "password": "newPassword",
  "role": ["user"]
}
```

##### Response
```json
{
  "message": "User registered successfully! Please verify your email."
}
```

#### `POST /api/auth/verify-email`

Verify email using otp

##### Verify email

```url
/api/auth/verify-email?email=newuser@example.com&otp=123456
```

##### Response (success)

```json
{
  "message": "Email verified successfully!"
}
```

##### Response (Invalid OTP)
```json
{
  "message": "Invalid or expired OTP"
}
```

## Project Structure

```bash
src/
├── main/
│   ├── java/
│   │   └── com/cosmicbook/search_and_rescue_droid/
│   │       ├── controller/       # REST controllers
│   │       ├── dto/              # Data Transfer Objects
│   │       ├── model/            # Entity models
│   │       ├── repository/       # Database repositories
│   │       ├── security/         # Security configuration
│   │       ├── service/          # Business logic services
│   │       └── config/           # Application configuration
│   └── resources/
│       ├── application.properties # Application settings
│       └── static/               # Static resources
└── test/                         # Unit and integration tests
```

## Contributing

This project is part of a college final year project and is **not open for external contributions or code usage** at this time. Please contact the project team for any inquiries.

---

## License

This project is **not open source** and is intended for academic purposes only. Redistribution or usage of the code outside the scope of this project is strictly prohibited.
