### учебный проект
##### RRoC: RESTful Register of Citizens (rest-сервер, позволяющий вести реестр жителей)

[проектный паспорт](https://github.com/SuvorovJA/RRoC/blob/master/%D0%A2%D0%B5%D1%81%D1%82%D0%BE%D0%B2%D0%BE%D0%B5%20%D0%B7%D0%B0%D0%B4%D0%B0%D0%BD%D0%B8%D0%B5%20Spring%20Boot.pdf)

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
  - начиная с коммита 8e034cb - реализация на spring-security-jwt, с отделением authserver в отдельный проект (репозиторий [SSJWT](https://github.com/SuvorovJA/SSJWT)). Authserver теперь требует отдельного запуска как [docker-микросервис](https://github.com/SuvorovJA/SSJWT/blob/master/docker/start_app.sh).
- PostgreSQL 10
  - оба проекта используют Postgresql. RRoC - использует database 'postgres'. SSJWT - использует database 'authserver'
- Flyway
- Mockito (транзитивно)

##### запуск тестов

- ru.sua.rroc.repository.CitizenRepositoryTest и ru.sua.rroc.service.CitizenServiceTest особой подготовки не требуют, используют docker-контейнер Postgresql10 и миграции из приложения. 

- ru.sua.rroc.controller.CitizenControllerTest использует ту же инстанцию Postgresql10, что и два вышеупомянутых тест-класса. И требует: 
  - предварительной сборки docker-образа [ssjwt:latest](https://github.com/SuvorovJA/SSJWT)/[docker/build_ssjwt_image.sh](https://github.com/SuvorovJA/SSJWT/blob/master/docker/build_ssjwt_image.sh). Authserver из ssjwt:latest при работе использует собственную инстанцию Postgresql10 недоступную снаружи compose-контейнера.
  - поскольку публикации в docker-hub получившегося образа не предполагается, то для запуска используется [локальный docker-compose](https://www.testcontainers.org/modules/docker_compose/#using-local-compose-mode), соответственно docker-compose должен присутствовать в системе. _На windows работоспособность не проверялась и не гарантируется._

  