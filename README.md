# ShortLink

A simple URL shortener built with **Java, Spring Boot, Spring Data JPA,
Hibernate, and PostgreSQL**.

ShortLink converts a long URL into a short, randomly generated Base62
code. When the short URL is opened, the application finds the original
URL and redirects the user to it.

This version is intentionally kept simple so the core Spring Boot flow
is easy to understand and extend later.

## Features

-   Create short URLs from valid HTTP/HTTPS URLs
-   Generate random 6-character Base62 short codes
-   Check for short-code collisions before saving
-   Store URLs in PostgreSQL
-   Use Spring Data JPA / Hibernate for database access
-   Redirect short URLs using HTTP 302
-   Simple HTML, CSS, and JavaScript frontend
-   Docker support
-   No authentication or Spring Security for now

## Features intentionally not included

The current version focuses on the core URL-shortening functionality.
The following can be added later:

-   User authentication and authorization
-   Custom aliases
-   Click analytics
-   Country, browser, device, and referrer tracking
-   Link expiration
-   Link deactivation
-   Redis/in-memory caching
-   Asynchronous click processing

## How It Works

``` text
                    User
                     |
                     v
                 Frontend
                     |
                     | POST /api/urls
                     v
                Controller
                     |
                     v
                  Service
                     |
                     | validate + generate code
                     v
                Repository
                     |
                     v
                PostgreSQL
```

For a redirect:

``` text
User opens /aX7kP2
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

## Technology Stack

Technology            Purpose
  --------------------- -------------------------------------------
Java 25               Programming language
Spring Boot 4.1       Backend framework
Spring Web MVC        REST APIs and HTTP handling
Spring Data JPA       Database access
Hibernate             ORM implementation
PostgreSQL            Relational database
HTML/CSS/JavaScript   Frontend
Maven                 Build and dependency management
Docker                Containerization
Docker Compose        Running application and database together

## Project Structure

``` text
ShortLink/
│
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

## Database

The application uses a single table:

``` text
short_links
```

with the main fields:

Field            Description
  ---------------- -------------------------------------
`code`           Unique short URL code
`original_url`   Original long URL
`created_at`     Time when the short URL was created

The `code` is used as the primary key.

Example:

``` text
code    original_url                         created_at
---------------------------------------------------------------
aX7kP2  https://www.example.com/products/...  2026-09-17...
9LmQ4z  https://github.com/                   2026-09-17...
```

## API

### 1. Create a Short URL

**POST**

``` text
/api/urls
```

Request:

``` json
{
  "url": "https://www.google.com"
}
```

Example response:

``` json
{
  "code": "aX7kP2",
  "shortPath": "/aX7kP2",
  "url": "https://www.google.com"
}
```

The generated code is random and uses Base62 characters:

``` text
0-9
a-z
A-Z
```

### 2. Redirect

**GET**

``` text
/{code}
```

Example:

``` text
http://localhost:8080/aX7kP2
```

The application looks up `aX7kP2` in PostgreSQL and returns an HTTP
`302 Found` response pointing to the original URL.

## URL Validation

Only valid HTTP and HTTPS URLs are accepted.

Valid examples:

``` text
https://www.google.com
https://github.com/Pratik23-10/ShortLink
http://example.com
```

Invalid examples:

``` text
google.com
hello
abc
```

Invalid URLs return:

``` text
400 Bad Request
```

## Random Short-Code Generation

The application generates a random 6-character Base62 code.

Example:

``` text
aX7kP2
9LmQ4z
K8v2Ra
pT6xW1
```

Before saving the link, the application checks whether the generated
code already exists:

``` text
Generate code
     |
     v
Does code exist?
   /       \
 Yes        No
  |          |
Generate     Save
again
```

This prevents short-code collisions.

## Local Setup

### Requirements

Install:

-   Java 25
-   PostgreSQL
-   Git
-   Docker Desktop (optional)

You can use the included Maven Wrapper, so Maven does not need to be
installed separately.

### 1. Create PostgreSQL Database

Create a database named:

``` sql
CREATE DATABASE demo;
```

The local configuration uses:

``` text
Host:     localhost
Port:     5432
Database: demo
Username: postgres
```

The password should be supplied through the `DB_PASSWORD` environment
variable.

### 2. Configure Environment Variable

Windows PowerShell:

``` powershell
$env:DB_PASSWORD="your_password"
```

Or configure `DB_PASSWORD` in your IntelliJ Run Configuration.

Do not put the real database password into Git.

### 3. Run the Application

Windows:

``` powershell
.\mvnw.cmd spring-boot:run
```

macOS/Linux:

``` bash
./mvnw spring-boot:run
```

Open:

``` text
http://localhost:8080
```

## Running with Docker

The project can also be run using Docker Compose.

Make sure Docker Desktop is running.

Create a local `.env` file in the project root:

``` env
DB_PASSWORD=your_password
```

Make sure `.env` is included in `.gitignore`.

Then run:

``` powershell
docker compose up --build
```

The application will be available at:

``` text
http://localhost:8080
```

When Spring Boot runs inside Docker and PostgreSQL is another Compose
service, the database hostname should be the Compose service name, for
example:

``` text
jdbc:postgresql://postgres:5432/dbs
```

Do not use `localhost` for the PostgreSQL hostname from inside the
Spring Boot container.

## Environment Variables

The application can use the following variables:

``` text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Example:

``` text
DB_URL=jdbc:postgresql://postgres:5432/dbs
DB_USERNAME=pratik
DB_PASSWORD=your_password
```

Keep credentials outside the source code.

## Testing the API

Using PowerShell:

``` powershell
Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8080/api/urls" `
  -ContentType "application/json" `
  -Body '{"url":"https://www.google.com"}'
```

Or use Postman.

Then open the returned short URL in your browser.

## Git and GitHub

Typical development workflow:

``` text
git pull
       |
       v
Create feature branch
       |
       v
Write code
       |
       v
Test
       |
       v
git status
       |
       v
git add
       |
       v
git commit
       |
       v
git push
```

Example:

``` bash
git checkout -b feature/random-short-code

git add .

git commit -m "Generate random Base62 short codes"

git push -u origin feature/random-short-code
```

## Security

Spring Security is intentionally not included in this version.

This project is currently designed for learning the fundamentals of:

-   REST APIs
-   Spring Boot
-   Dependency Injection
-   Service and Repository layers
-   JPA/Hibernate
-   PostgreSQL
-   HTTP redirects
-   Docker
-   Git/GitHub

Authentication and authorization can be added later.

## Future Improvements

Possible future versions:

### Version 2

-   Better request validation
-   Global exception handling
-   Improved API responses

### Version 3

-   Redis caching

### Version 4

-   Spring Security
-   User accounts
-   User-owned URLs

### Version 5

-   Click analytics
-   Click events
-   Browser/device/referrer information

### Version 6

-   Link expiration
-   Link deactivation
-   Custom aliases

## Learning Goal

The main goal of this project is to understand the complete Spring Boot
request flow:

``` text
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

Once this flow is understood, additional features such as
authentication, caching, analytics, and expiration can be added one at a
time.

## Author

**Pratik Yadav**

Built as a learning project while learning Java, Spring Boot,
PostgreSQL, Docker, and Git/GitHub.
h t t p s : / / g i t h u b . c o m / P r a t i k 2 3 - 1 0 / S h o r t U r l . g i t  
 