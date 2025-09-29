# Ресурсы

application.yml, настройки логов, конфигурации Liquibase и прочее.

# Конфигурация приложения (application.yml)

## Описание

Файл конфигурации Spring Boot приложения, содержащий настройки для работы с базой данных PostgreSQL и инструменты миграции.

## Основные секции

### Spring DataSource

Конфигурация подключения к базе данных PostgreSQL. Параметры подключения:

* **URL**: `jdbc:postgresql://localhost:5432/bankdb`
* **Пользователь**: `bank`
* **Драйвер**: `org.postgresql.Driver`
* **Пароль**: `bankpass`

### Spring JPA

Настройка Hibernate:

* Параметр `ddl-auto: none` указывает, что автоматическая генерация схемы базы данных отключена

### Liquibase

Инструмент для управления миграциями базы данных:

* Основной файл миграций находится по пути: `classpath:db/migration/db.changelog-master.yaml`
