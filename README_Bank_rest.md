# Система управления банковскими картами

## Структура проекта

- **`docker-compose.yml`**  
  Скрипт для создания и запуска контейнера с PostgreSQL 16. Настраивает БД и порты для приложения.

- **`src/main/resources`**  
  Конфигурации приложения: `application.yml` или `application.properties`, миграции Flyway, настройки логирования и CORS.

- **`pom.xml`**  
  Maven-зависимости проекта, плагины сборки и настройки компиляции.

- **`src/main/java/.../controller`**  
  REST-контроллеры для работы с картами и транзакциями.

- **`src/main/java/.../service`**  
  Сервисный слой: бизнес-логика операций с картами и транзакциями.

- **`src/main/java/.../repository`**  
  Интерфейсы для работы с базой данных через Spring Data JPA.

- **`src/main/java/.../security`**  
  Конфигурация Spring Security, JWT, фильтры аутентификации и авторизации.

- **`src/main/java/.../model`**  
  Сущности базы данных (User, Card, Transaction) и DTO для передачи данных.

- **`src/main/java/.../exception`**  
  Кастомные исключения (например, `CardNotFoundException`, `InsufficientFundsException`) и обработчики ошибок.
