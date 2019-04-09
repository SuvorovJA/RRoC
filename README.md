### учебный проект
RRoC: RESTful register of citizens (rest-сервер, позволяющий вести реестр жителей)

Стек технологий
- JDK ~1.8~ 1.11
- Spring Boot (последней версии (2.1.3))
- Data JPA + Repository
- WebMVC
- Security
- JWT
  - в коммите e4e84ee - реализация на io.jsonwebtoken.jjwt
  - начиная с коммита "этого" - реализация на spring-security-jwt, с отделением authserver в отдельный проект (репозиторий SSJWT). Authserver теперь требует отдельного запуска, в том числе и до тестов. 
- PostgreSQL 10
  - оба проекта используют Postgresql. RRoC - database 'postgres'. SSJWT - database 'authserver'
- Flyway
- Mockito (транзитивно)