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
- Java 17 or higher  
- Maven 3.6+  
- (Optional) Docker  

### Setup

Clone the repository:  
```bash
git clone https://github.com/autonomous-search-and-rescue-droid/spring-backend.git  
cd spring-backend  
```

Build the project:
```bash
mvn clean install  
```

Run the application:
```bash
mvn spring-boot:run  
```

Access the API at: <http://localhost:8080>

### Running With Docker

Build the Docker image:
```bash
docker build -t spring-backend .  
```

Run the container:
```bash
docker run -p 8080:8080 spring-backend  
```

## API Documentation

This section provides details on the available API endpoints for interacting with the application.

### Base URL

All API endpoints are prefixed with: `http://localhost:8080` (or your deployed API base URL).

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

##### Response
```json
{
  "token": "jwt-token",
  "id": 1,
  "username": "exampleUser",
  "email": "user@example.com",
  "roles": ["ROLE_USER"]
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
  "message": "User registered successfully!"
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
