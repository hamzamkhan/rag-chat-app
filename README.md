### RAG Chat App

A Spring Boot microservice for storing chat sessions, messages, and basic user data for Retrieval-Augmented Generation (RAG) chat applications. It exposes REST APIs for managing users, chat sessions, and chat messages, and is packaged with Docker and PostgreSQL for easy local development.

---

### Features

- **User management**: Create users that can own chat sessions.
- **Chat sessions**: Create, rename, favorite, and delete chat sessions.
- **Chat messages**: Store messages and associated RAG context for each session.
- **PostgreSQL persistence**: Uses PostgreSQL via Spring Data JPA.
- **Validation & error handling**: Jakarta Bean Validation and centralized exception handling.
- **Rate limiting**: Bucket4j-based rate limiting (see `RateLimiterConfig`).
- **API key auth**: Simple API key filter to protect the APIs.
- **OpenAPI / Swagger UI**: Auto-generated API docs via Springdoc.
- **Dockerized**: Multi-stage Docker build plus `docker-compose` for app + DB + pgAdmin.


### Project Structure

- **`src/main/java/com/app/ragchatapp`**
  - **`RagChatAppApplication`**: Spring Boot entry point.
  - **`chat_session`**: Chat session entity, DTOs, repository, service, and `ChatSessionController`.
  - **`chat_message`**: Chat message + context entities, DTOs, repository, service, and `ChatMessageController`.
  - **`user`**: User entity, DTOs, repository, service, and `UserController`.
  - **`config`**: API key filter/config, rate limiter, OpenAPI config, seed data.
  - **`aop`**: Logging aspect and global error handling response model.
  - **`exception`**: Custom exception types.
- **`src/main/resources/application.properties`**: Core application configuration.
- **`docker-compose.yml`**: PostgreSQL, pgAdmin, and app services.
- **`Dockerfile`**: Multi-stage build for a slim runtime image.

---

---

### Configuration

The application is primarily configured via `application.properties` and environment variables.

- **Server**
  - Port: `8090` (exposed in Dockerfile)

- **Database (PostgreSQL)**
  - URL: `spring.datasource.url=jdbc:postgresql://db:5432/ragchat`
  - Username: `ragchat`
  - Password: `ragchat`
  - JPA DDL: `spring.jpa.hibernate.ddl-auto=create` (creates schema on startup; adjust for production).

- **Rate Limiting**
  - `ragchat.rate-limit.capacity` (default `100`)
  - `ragchat.rate-limit.refill-tokens` (default `100`)
  - `ragchat.rate-limit.refill-seconds` (default `60`)

- **CORS**
  - `cors.allowed-origins=${CORS_ALLOWED_ORIGINS:*}`
  - `cors.allowed-methods=GET,POST,PUT,PATCH,DELETE,OPTIONS`

- **Actuator**
  - Exposed endpoints: `health`, `info`

- **OpenAPI / Swagger**
  - `springdoc.api-docs.enabled=true`
  - `springdoc.swagger-ui.enabled=true`

There is an `.env.example` file in the repo you can copy to `.env` (or use directly with Docker Compose) and fill in any sensitive values like `API_KEY`.


### Running with Docker & Docker Compose

The repository includes a multi-stage `Dockerfile` and `docker-compose.yml` for local development.

1. **Build and start services**

   ```bash
   docker-compose up --build
   ```

   This will start:
   - **db**: PostgreSQL 16 (`ragchat-db`) on host port `5432`.
   - **pgadmin**: pgAdmin (`ragchat-pgadmin`) on host port `5050`.
   - **app**: RAG Chat App (`ragchat-app`), built from this project.

2. **Configure environment**

   - Optionally create a `.env` file to pass environment variables to Docker Compose (e.g. `API_KEY`, `CORS_ALLOWED_ORIGINS`).

3. **Access services**

   - App: `http://localhost:8090`
   - Swagger UI: `http://localhost:8090/swagger-ui/index.html`
   - pgAdmin: `http://localhost:5050` (default email `admin@example.com`, password `admin123` from `docker-compose.yml`).

---

### Key REST Endpoints (v1)

All endpoints are prefixed with `/v1`. The exact request/response payloads are documented in Swagger UI.

- **Users** (`UserController`)
  - `POST /v1/user` – Create a user.

- **Chat Sessions** (`ChatSessionController`)
  - `POST /v1/session` – Create a chat session.
  - `PATCH /v1/session/{id}/rename?title=...` – Rename a session.
  - `PATCH /v1/session/{id}/favorite?favorite=true|false` – Mark/unmark as favorite.
  - `DELETE /v1/session/{id}` – Delete a session.

- **Chat Messages** (`ChatMessageController`)
  - `POST /v1/message` – Add a new message to a session.
  - `GET /v1/message/{sessionId}?page=0&size=50` – Get paginated messages for a session (sorted by `createdAt` ascending).

Additional endpoints (health checks, etc.) are exposed via Spring Boot Actuator and are visible in the OpenAPI documentation.

---

### Testing

Run tests using Maven:

```bash
./mvnw test
# or
mvn test
```

---

### API Documentation

OpenAPI metadata is configured in `OpenApiConfig`:

- **Title**: `RAG Chat App`
- **Description**: `Microservice for storing chat sessions and messages`
- **Version**: `v1`

Swagger UI is available (when enabled) at:

- `http://localhost:8090/swagger-ui/index.html`