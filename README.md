# LogiTrack — Thymeleaf edition (no JavaScript)

A server-rendered logistics and shipment tracking application using Java 21, Spring Boot, Maven, Thymeleaf, HTML, CSS, Spring Data JPA and MySQL. There are no `.js` files or JavaScript dependencies.

## Prerequisites
- JDK 21
- Eclipse IDE (Enterprise Java/Web Developers recommended)
- MySQL 8.x

## Setup
1. Create the database:
   ```sql
   CREATE DATABASE logistics_db;
   ```
2. Open `src/main/resources/application.properties` and replace `change_me` with your MySQL password, or set `DB_USERNAME` and `DB_PASSWORD` environment variables.
3. Eclipse: File > Import > Maven > Existing Maven Projects > select the extracted project folder.
4. Right-click project > Maven > Update Project.
5. Run `com.example.logitrack.LogitrackApplication`.
6. Open http://localhost:8080.

## Features
- Dashboard with shipment summary counts
- Server-side search and status filtering
- Create shipment with generated tracking number
- Update shipment status in a controlled sequence
- Chronological tracking history
- Validation messages and basic error pages
- Responsive HTML/CSS design

## Status flow
`CREATED -> DISPATCHED -> IN_TRANSIT -> OUT_FOR_DELIVERY -> DELIVERED`

## Database
Hibernate creates/updates the `shipments` and `tracking_events` tables when the application starts (`spring.jpa.hibernate.ddl-auto=update`). For production, use migrations such as Flyway or Liquibase instead.

## Important
This is a learning project. It does not include authentication, authorization, real courier integrations, or production-grade locking against concurrent status updates. Forms submit to the server and reload the page by design; JavaScript is not used.
