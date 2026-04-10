# FestLog

A music festival and concert experience tracker built as a Spring Boot 4 project with an MVC interface and a REST API.

## Technologies

| Component | Version |
|---|---|
| Java | 25 |
| Spring Boot | 4.0.3 |
| Spring MVC / Spring Security | 7.x |
| Thymeleaf | 3.x |
| Spring Data JPA / Hibernate | 7.x |
| H2 (in-memory) | runtime |
| Auth0 Java JWT | 4.4.0 |
| springdoc-openapi | 3.0.0 |

## Running the Project in IntelliJ IDEA

1. **Open the project:** `File → Open` → select the `festlog` folder
2. **SDK:** `File → Project Structure → SDK` → set to **Java 25**
3. **Maven:** IntelliJ will automatically download dependencies; if not, run **Reload Maven Project**
4. **Run:** `FestlogApplication.java` → right-click → *Run*
5. **Access:** [http://localhost:8080](http://localhost:8080)

## Default User Accounts (in-memory H2)

| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | ADMIN — full management |
| `user` | `user123` | USER — read and search only |

## MVC Interface

| URL | Description | Access |
|---|---|---|
| `/` | Redirect to `/events` | — |
| `/auth/login` | Login form | Public |
| `/auth/register` | Registration form | Public |
| `/events` | Browse and search events | USER + ADMIN |
| `/events/{id}` | Event details | USER + ADMIN |
| `/events/new` | New event form | ADMIN |
| `/events/edit/{id}` | Edit event form | ADMIN |
| `/events/delete/{id}` | Delete event (POST) | ADMIN |

## REST API

Base URL: `/api`

### Authentication

| Method | URL | Description |
|---|---|---|
| `POST` | `/api/auth/login` | Login — returns access + refresh token |
| `POST` | `/api/auth/register` | Register a new user account |
| `POST` | `/api/auth/refresh` | Obtain a new access token |
| `POST` | `/api/auth/logout` | Revoke the refresh token |

### Events (requires Bearer token)

| Method | URL | Description | Role |
|---|---|---|---|
| `GET` | `/api/events` | All events | USER + ADMIN |
| `GET` | `/api/events/{id}` | Single event | USER + ADMIN |
| `GET` | `/api/events/search` | Search (`query`, `type`, `status`, `city`) | USER + ADMIN |
| `POST` | `/api/events` | Create new event | ADMIN |
| `PUT` | `/api/events/{id}` | Update event | ADMIN |
| `DELETE` | `/api/events/{id}` | Delete event | ADMIN |

### Example: Login and API Usage

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Fetch events using the access token
curl http://localhost:8080/api/events \
  -H "Authorization: Bearer <access_token>"
```

## Swagger UI

Available at: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

1. Call `POST /api/auth/login` with `admin` / `admin123`
2. Copy the `accessToken` from the response
3. Click **Authorize** (top right) → paste the token
4. Use any of the secured endpoints

## H2 Console

URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:festlogdb` |
| Username | `sa` |
| Password | *(leave blank)* |

## Project Structure

```
src/main/java/hr/algebra/festlog/
├── FestlogApplication.java
├── config/
│   ├── DataInitializer.java      # Sample data on startup
│   ├── OpenApiConfig.java        # Swagger / OpenAPI configuration
│   └── SecurityConfig.java       # Two filter chains (API + MVC)
├── controller/
│   ├── mvc/
│   │   ├── AuthMvcController.java
│   │   ├── EventMvcController.java
│   │   └── HomeController.java
│   └── rest/
│       ├── AuthRestController.java
│       └── EventRestController.java
├── dto/
│   ├── EventDto.java              # Java record
│   └── Dto.java                   # Login/Register/Token records
├── entity/
│   ├── Event.java
│   ├── RefreshToken.java
│   └── User.java                  # Implements UserDetails
├── enums/
│   ├── EventStatus.java
│   ├── EventType.java
│   └── Role.java
├── repository/
│   ├── EventRepository.java
│   ├── RefreshTokenRepository.java
│   └── UserRepository.java
├── security/
│   ├── JwtAuthFilter.java
│   ├── JwtService.java
│   └── UserDetailsServiceImpl.java
└── service/
    ├── AuthService.java
    ├── EventService.java
    └── RefreshTokenService.java
```
