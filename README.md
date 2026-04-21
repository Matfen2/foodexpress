# FoodExpress

[![CI](https://github.com/Matfen2/foodexpress/actions/workflows/ci.yml/badge.svg)](https://github.com/Matfen2/foodexpress/actions/workflows/ci.yml)
[![Docker](https://github.com/Matfen2/foodexpress/actions/workflows/publish.yml/badge.svg)](https://github.com/Matfen2/foodexpress/actions/workflows/publish.yml)

REST API de commande et livraison de nourriture — Spring Boot 4 / PostgreSQL / Docker.

## Démo live

- **API** : https://foodexpress-api-2xp2.onrender.com/api/restaurants
- **Swagger UI** : https://foodexpress-api-2xp2.onrender.com/swagger-ui.html
- **Health check** : https://foodexpress-api-2xp2.onrender.com/actuator/health

## Stack

- **Backend** : Java 17, Spring Boot 4, Spring Data JPA, MapStruct, Lombok
- **Base de données** : PostgreSQL 18 (prod sur Render), H2 (dev), migrations Flyway
- **Qualité** : JUnit 5, Mockito, MockMvc, AssertJ
- **Infra** : Docker multi-stage, Docker Compose, GitHub Actions CI/CD, GHCR
- **Hébergement** : Render (Web Service + Managed PostgreSQL)
- **Observabilité** : Spring Boot Actuator, SpringDoc OpenAPI (Swagger UI)

## Démarrage local

```bash
git clone https://github.com/Matfen2/foodexpress.git
cd foodexpress
docker compose up --build
```

Une fois les containers démarrés :

- API : http://localhost:8080
- Swagger : http://localhost:8080/swagger-ui.html
- Health : http://localhost:8080/actuator/health

## Architecture

- Event-driven via `ApplicationEventPublisher` (`@Async` + `@EventListener`)
- Pagination Spring Data sur les endpoints de liste
- Gestion centralisée des erreurs avec `@RestControllerAdvice`
- Validation Bean Validation (`@Valid`, `@NotBlank`, etc.)
- Migrations de schéma versionnées (V1 → V4)

## Pipeline de livraison
