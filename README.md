# Recommendation Service

## Обзор проекта
Recommendation Service — это Spring Boot приложение для выдачи персонализированных рекомендаций банковских продуктов на основе истории транзакций пользователей, статических правил и динамических правил, управляемых через REST API.

Проект разработан как курсовой проект рекомендательной системы для банка.

Сервис поддерживает:
- выдачу рекомендаций через REST API;
- управление динамическими правилами рекомендаций;
- получение статистики срабатывания динамических правил;
- Telegram bot для выдачи рекомендаций по username;
- management endpoints для очистки кеша и получения информации о сервисе.

## Стек технологий
- Java 17
- Spring Boot
- Spring Web
- Spring Data JDBC / JdbcTemplate
- Spring Data JPA
- PostgreSQL
- H2 Database
- Liquibase
- Caffeine Cache
- Telegram Bot API (pengrad)
- Maven

## Основные возможности
- `GET /recommendation/{user_id}` — вернуть рекомендации для пользователя
- `POST /rule` — создать динамическое правило рекомендации
- `GET /rule` — получить список всех динамических правил
- `DELETE /rule/{id}` — удалить динамическое правило
- `GET /rule/stats` — получить статистику срабатывания динамических правил
- `POST /management/clear-caches` — очистить кеш рекомендаций
- `GET /management/info` — получить имя и версию сервиса
- команда Telegram bot `/recommend username` — вернуть рекомендации по username

## Базы данных
Проект использует две базы данных:
- **H2** — read-only база знаний с пользователями, продуктами и транзакциями
- **PostgreSQL** — хранилище динамических правил и статистики их срабатывания

## Сборка и запуск

Собрать проект:
```bash
.\mvnw.cmd package
```

Запустить приложение:
```bash
java -jar target/recommendation-service-0.0.1-SNAPSHOT.jar
```

## Сборка и запуск

Собрать проект:
```bash
.\mvnw.cmd package
```

Запустить приложение:
```bash
java -jar target/recommendation-service-0.0.1-SNAPSHOT.jar
```

## Документация

Проектная документация размещается в wiki репозитория и включает:
- главную страницу проекта;
- требования в формате User Story;
- traceability matrix;
- описание архитектуры и диаграммы;
- документацию REST API;
- инструкцию по развертыванию.

## Авторы

SkykaSmertnay