# Spring Backend

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
- Database support for mission and droid data
- Configurable and extensible Spring Boot architecture

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- (Optional) Docker

### Setup

1. Clone the repository:

    ```bash
    git clone https://github.com/autonomous-search-and-rescue-droid/spring-backend.git
    cd spring-backend
    ```

2. Build the project:

    ```bash
    mvn clean install
    ```

3. Run the application:

    ```bash
    mvn spring-boot:run
    ```

4. Access API at: `http://localhost:8080`

### Running with Docker

```bash
docker build -t spring-backend .
docker run -p 8080:8080 spring-backend
