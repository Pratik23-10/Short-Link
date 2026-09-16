# Simple URL Shortener

A simplified version of the original ShortLink project for learning Spring Boot.

## Features

- Create a short URL from a valid HTTP/HTTPS URL
- Generate a short Base62 code automatically
- Store links in PostgreSQL using Spring Data JPA/Hibernate
- Redirect from the short code to the original URL
- Simple HTML/CSS/JavaScript frontend
- No authentication/security for now

## Project flow

```text
Frontend
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
PostgreSQL
```

## Requirements

- Java 25
- Maven (or use the included Maven wrapper)
- PostgreSQL

## PostgreSQL setup

Create a database named `demo`:

```sql
CREATE DATABASE demo;
```

The default configuration expects:

- Host: `localhost`
- Port: `5432`
- Database: `demo`
- Username: `postgres`
- Password: `postgres`

You can change these in `src/main/resources/application.properties` or set `DB_PASSWORD` as an environment variable.

## Run

From the project folder:

### Windows

```powershell
.\mvnw.cmd spring-boot:run
```

### macOS/Linux

```bash
./mvnw spring-boot:run
```

Then open:

```text
http://localhost:8080
```

## Test with API

Create:

```http
POST http://localhost:8080/api/urls
Content-Type: application/json

{"url":"https://www.google.com"}
```

Response:

```json
{
  "code": "g8",
  "shortPath": "/g8",
  "url": "https://www.google.com"
}
```

Open `http://localhost:8080/g8` and the application will return a `302` redirect to Google.
