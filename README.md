# rental

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ARCN-Peach_library-fine&metric=alert_status&token=def867db9ec5eb3edf8a65f66d9e1b5b7df0d73b)](https://sonarcloud.io/summary/new_code?id=ARCN-Peach_library-fine)

Microservicio de prestamos para la biblioteca.

## Endpoints principales

- `POST /api/v1/rentals` crea un prestamo
- `PUT /api/v1/rentals/{rentalId}/return` registra la devolucion
- `GET /api/v1/rentals/{rentalId}` consulta un prestamo
- `GET /api/v1/rentals/users/{userId}` lista los prestamos de un usuario
- `GET /api/v1/rentals/overdue` lista prestamos vencidos

## Notas

La aplicacion ya esta preparada para persistencia con JPA/PostgreSQL y publicacion de eventos en RabbitMQ. La logica de negocio sigue separada de la infraestructura mediante puertos y casos de uso, y el test principal del flujo de prestamo usa Mockito.

## Levantar con Docker

```bash
docker compose up --build
```

Servicios y puertos:

- API: http://localhost:8083
- Actuator health: http://localhost:8083/actuator/health
- RabbitMQ Management: http://localhost:15672 (guest / guest)
- PostgreSQL: localhost:5432

## Validacion local

```bash
mvn test
```
