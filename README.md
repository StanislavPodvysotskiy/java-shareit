# ShareIt

A REST service for sharing things: users list items they're ready to lend, others search, book
them for a period and leave comments after use. If the item isn't there, a user can post a request
and owners can answer it with a new item. Course project (Yandex Practicum).

## Architecture

Two Spring Boot applications and PostgreSQL, run with Docker Compose:

- **gateway** (port 8080) — the public API: validates input and forwards valid requests to the
  server through `RestTemplate` clients;
- **server** (port 9090) — business logic and persistence (Spring Data JPA, PostgreSQL);
- **db** — PostgreSQL 13.

The current user is passed in the `X-Sharer-User-Id` header.

## API

| Resource | What it does |
|---|---|
| `/users` | create, update, get, delete users |
| `/items` | add and edit items, get an item with its last and next booking, owner's items, text search, `POST /items/{id}/comment` |
| `/bookings` | book an item, approve/reject as owner, get bookings of a booker or an owner filtered by state: `ALL`, `CURRENT`, `PAST`, `FUTURE`, `WAITING`, `REJECTED` |
| `/requests` | post a request for an item, get own requests and other users' requests with paging |

## Run

```bash
mvn clean package
docker compose up
```

Tests: 29 test classes in `server` (unit with Mockito, `@WebMvcTest`, `@DataJpaTest`,
`@SpringBootTest` integration), a Postman collection in `postman/sprint.json`, and the course's API
tests running in GitHub Actions on pull requests.

## Tech

Java, Spring Boot 2.7, Spring Data JPA, PostgreSQL, Lombok, Docker Compose, JUnit 5, Mockito.
