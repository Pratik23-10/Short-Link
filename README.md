# ShortLink

A simple and lightweight URL shortener built with **Java, Spring Boot, Spring Data JPA, Hibernate, and PostgreSQL**.

ShortLink converts long URLs into short, randomly generated links that are easy to share. When a user opens a short link, the application looks up the original URL and redirects them to the destination.

## Features

- Create short URLs from valid HTTP/HTTPS links
- Generate random 6-character Base62 short codes
- Check for short-code collisions
- Store URL mappings in PostgreSQL
- Redirect short links using HTTP 302
- Simple and responsive web interface
- REST API
- Docker and Docker Compose support

## Tech Stack

- **Java**
- **Spring Boot**
- **Spring Web**
- **Spring Data JPA**
- **Hibernate**
- **PostgreSQL**
- **HTML / CSS / JavaScript**
- **Maven**
- **Docker**
- **Docker Compose**

## Architecture

ShortLink follows a simple layered architecture:

```text
Client / Frontend
       |
       v
   Controller
       |
       v
     Service
       |
       v
   Repository
       |
       v
   PostgreSQL
```

### Create URL

```text
User enters long URL
        |
        v
POST /api/urls
        |
        v
Controller
        |
        v
Service
  |     |
  |     +--> Validate URL
  |
  +--------> Generate random code
        |
        v
Repository
        |
        v
PostgreSQL
        |
        v
Short URL returned
```

### Redirect

```text
User opens short URL
        |
        v
GET /{code}
        |
        v
Controller
        |
        v
Service
        |
        v
Repository
        |
        v
PostgreSQL
        |
        v
Original URL
        |
        v
HTTP 302 Redirect
```

## Project Structure

```text
ShortLink/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/pratik/demourl/
│       │       ├── controller/
│       │       │   └── ShortUrlController.java
│       │       ├── model/
│       │       │   └── LinkRecord.java
│       │       ├── repository/
│       │       │   └── LinkRecordRepository.java
│       │       ├── service/
│       │       │   └── ShortUrlService.java
│       │       └── DemourlApplication.java
│       │
│       └── resources/
│           ├── static/
│           │   └── index.html
│           └── application.properties
│
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── .gitignore
└── README.md
```

## API Documentation

### Create Short URL

**Endpoint**

```http
POST /api/urls
```

**Request**

```json
{
  "url": "https://www.google.com"
}
```

**Example Response**

```json
{
  "code": "aX7kP2",
  "shortPath": "/aX7kP2",
  "url": "https://www.google.com"
}
```

The generated code can then be used as:

```text
http://localhost:8080/aX7kP2
```

### Redirect to Original URL

**Endpoint**

```http
GET /{code}
```

**Example**

```text
GET /aX7kP2
```

The application finds the corresponding URL in PostgreSQL and redirects the user to the original destination.

## Short Code Generation

ShortLink uses a random 6-character **Base62** code.

Base62 contains:

```text
0-9
a-z
A-Z
```

Example generated codes:

```text
aX7kP2
9LmQ4z
K8v2Ra
pT6xW1
```

Before saving a generated code, the application checks whether the code already exists in the database. If it does, another code is generated.

## Database

The application uses PostgreSQL to store URL mappings.

The main data fields are:

| Field | Description |
|---|---|
| `code` | Unique short code |
| `original_url` | Original URL |
| `created_at` | Creation timestamp |

Example:

| code | original_url |
|---|---|
| `aX7kP2` | `https://www.google.com` |
| `9LmQ4z` | `https://github.com` |

## URL Validation

ShortLink accepts valid HTTP and HTTPS URLs.

Valid:

```text
https://www.google.com
https://github.com
http://example.com
```

Invalid:

```text
google.com
hello
abc
```

Invalid requests are rejected instead of being stored.

## Running Locally

### Prerequisites

Install:

- Java
- PostgreSQL
- Git
- Docker Desktop (optional)

The project includes the Maven Wrapper, so Maven does not need to be installed separately.

### 1. Create the Database

Create a PostgreSQL database:

```sql
CREATE DATABASE demo;
```

### 2. Configure Database Environment Variables

The application uses:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Example:

```text
DB_URL=jdbc:postgresql://localhost:5432/demo
DB_USERNAME=postgres
DB_PASSWORD=your_password
```

Keep the actual password outside the source code.

### 3. Run the Application

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

Open:

```text
http://localhost:8080
```

## Running with Docker

Make sure Docker Desktop is running.

Create a local `.env` file in the project root:

```env
DB_PASSWORD=your_password
```

Do **not** commit `.env` to GitHub.

Then run:

```bash
docker compose up --build
```

The application will be available at:

```text
http://localhost:8080
```

When Spring Boot and PostgreSQL are running as Docker Compose services, the database hostname should be the Compose service name:

```text
jdbc:postgresql://postgres:5432/dbs
```

rather than `localhost`.

## Environment Variables

Keep database credentials outside the source code.

Example:

```text
DB_URL=jdbc:postgresql://postgres:5432/dbs
DB_USERNAME=pratik
DB_PASSWORD=your_password
```

For local development, these values can be supplied through your operating-system environment variables, IntelliJ Run Configuration, or Docker Compose.

## GitHub

The project is designed to be version controlled with Git.

Typical workflow:

```bash
git pull

git checkout -b feature/my-feature

# Make changes and test

git status
git add .
git commit -m "Describe the change"
git push -u origin feature/my-feature
```

Before pushing, make sure sensitive files such as `.env` are ignored.

## Security

Authentication and authorization are intentionally not part of the current version.

The current application focuses on the core URL-shortening functionality. User authentication and other security features can be introduced in a future version.

## Future Improvements

Planned or possible improvements include:

- User registration and login
- Spring Security
- JWT authentication
- Custom aliases
- URL expiration
- Link deactivation
- Click analytics
- Browser and device analytics
- Referrer tracking
- Redis caching
- Rate limiting
- Improved API documentation with Swagger / OpenAPI

## Learning Goals

This project is also designed as a practical Spring Boot learning project.

The main backend flow is:

```text
HTTP Request
     |
     v
Controller
     |
     v
Service
     |
     v
Repository
     |
     v
JPA / Hibernate
     |
     v
PostgreSQL
     |
     v
HTTP Response
```

The project demonstrates how a Spring Boot application can receive an HTTP request, process business logic, communicate with a database, and return an HTTP response.

## Author

**Pratik Yadav**

Built as a practical project while learning Java, Spring Boot, PostgreSQL, Docker, and Git/GitHub.
