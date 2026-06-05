### Локальный запуск тестов
1. Склонировать репозиторий
2. В корне проекта создать файл .env на основе примера env.example
3. Вставить OAuth-токен в переменную OAuth_TOKEN внутри файла .env
4. Запустить все тесты командой:

    Для Linux/macOS: `./mvnw clean test`

    Для Windows: `.\mvnw.cmd clean test`

5. Если установлен maven: `mvn clean test`