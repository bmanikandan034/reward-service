# Rewards Service
This is a simple Spring Boot REST API that calculates reward points for customers based on their transactions.

The goal of this project is to demonstrate clean architecture, proper exception handling, logging, and good API design practices.
## Tech Stack
- Java 17
- Spring Boot 3
- Spring Data JPA
- H2 (in-memory database)
- Lombok
- SpringDoc (Swagger)
- Maven

## Reward Calculation Logic

The reward calculation follows the standard rule:

- 2 points for every dollar spent above $100
- 1 point for every dollar spent between $50 and $100
- No points for amounts below $50

Example:
- $120 → 90 points
- $75 → 25 points
- $40 → 0 points
Rewards are calculated per month and also aggregated as total.

## How to Run
1. Build the project

Using Maven wrapper:
mvnw.cmd clean install
2. Run the application
mvnw.cmd spring-boot:run

Swagger Documentation

http://localhost:8080/swagger-ui.html

H2 Database Console
http://localhost:8080/h2-console

Connection details:

- JDBC URL: jdbc:h2:mem:testdb
- Username: sa
- Password: (leave blank)

Tables are auto-created using Hibernate and sample data is loaded at startup.
