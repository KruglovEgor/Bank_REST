# Система управления банковскими картами

Backend-приложение на Java Spring Boot для управления банковскими картами с аутентификацией через JWT, ролями `ADMIN`/`USER`, CRUD для карт и переводами между своими картами.

## Требования
- Java 17+
- Maven 3.9+
- Docker (для локальной БД PostgreSQL)

## Запуск
1) Запуск PostgreSQL через Docker:
```bash
docker compose up -d db
```

2) Запуск приложения локально (использует настройки из `application.yml`):
```bash
mvn spring-boot:run
```

Либо собрать jar и запускать:
```bash
mvn -DskipTests package && java -jar target/bankcards-0.0.1-SNAPSHOT.jar
```

Swagger UI: `http://localhost:8080/swagger-ui.html`

## Конфигурация
- `src/main/resources/application.yml` — настройки datasource, Liquibase, JWT, Swagger
- Liquibase changelog: `src/main/resources/db/changelog/changelog-master.yaml`
- Docker Compose: `docker-compose.yml` (сервисы `db`, `app`)

Переменные окружения (по умолчанию заданы):
- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`
- `JWT_SECRET`, `JWT_EXP_MIN`
- `CARD_SECRET`

## Аутентификация
- Регистрация: `POST /auth/register` { username, password, fullName }
- Логин: `POST /auth/login` { username, password } → { token }

Передавайте токен в `Authorization: Bearer <token>` для защищённых эндпоинтов.

## Основные эндпоинты
- Карты
  - `POST /cards` — создать карту текущему пользователю
  - `GET /cards` — список карт текущего пользователя (поддерживает `Pageable`)
  - `POST /cards/{id}/status?status=ACTIVE|BLOCKED|EXPIRED` — смена статуса своей карты
  - `POST /cards/admin/{id}/status?status=...` — смена статуса администратором (ROLE_ADMIN)

- Переводы
  - `POST /transfers` — перевод между своими картами { fromCardId, toCardId, amountMinor }
  - `GET /transfers` — история переводов текущего пользователя (пагинация)

## Безопасность и хранение
- JWT (HS256), срок жизни настраивается `jwt.expiration-minutes`
- Номер карты хранится в БД в зашифрованном виде (AES-GCM); в ответах используется маска `**** **** **** 1234`
- Ролевой доступ: `ROLE_USER`, `ROLE_ADMIN`

## Стек
Java 17, Spring Boot 3, Spring Security, Spring Data JPA, PostgreSQL, Liquibase, Docker, JWT, Springdoc OpenAPI

## Заметки по доработкам
- Добавить аннотации OpenAPI к контроллерам и DTO
- Дополнить тестами сервисов и контроллеров
- Реализовать управление пользователями для `ADMIN` (создание/блокировка и т.д.)
