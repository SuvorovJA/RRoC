### учебный проект
##### RRoC: RESTful Register of Citizens (rest-сервер, позволяющий вести реестр жителей)

[проектный паспорт](/Тестовое задание Spring Boot.pdf)

[основной tasklist по проекту](/TASKLIST.md)

[tasklist рефакторинга](/TASKLIST.RFR.md)


Стек технологий
- JDK ~1.8~ 1.11
- Spring Boot (последней версии (2.1.3))
- Data JPA + Repository
- WebMVC
- Security
- JWT
  - в коммите e4e84ee - реализация на io.jsonwebtoken.jjwt
  - начиная с коммита 8e034cb - реализация на spring-security-jwt, с отделением authserver в отдельный проект (репозиторий [SSJWT](/SSJWT)). Authserver теперь требует отдельного запуска, в том числе и до тестов. Фактически - микросервис. 
- PostgreSQL 10
  - оба проекта используют Postgresql. RRoC - использует database 'postgres'. SSJWT - использует database 'authserver'
- Flyway
- Mockito (транзитивно)