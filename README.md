# FoodExpress

[![CI](https://github.com/Matfen2/foodexpress/actions/workflows/ci.yml/badge.svg)](https://github.com/Matfen2/foodexpress/actions/workflows/ci.yml)
[![Docker](https://github.com/Matfen2/foodexpress/actions/workflows/publish.yml/badge.svg)](https://github.com/Matfen2/foodexpress/actions/workflows/publish.yml)

REST API de commande et livraison de nourriture - Spring Boot 4 / PostgreSQL / Docker.

## Démo live

- **API** : https://foodexpress-api-2xp2.onrender.com/api/restaurants
- **Swagger UI** : https://foodexpress-api-2xp2.onrender.com/swagger-ui.html
- **Health check** : https://foodexpress-api-2xp2.onrender.com/actuator/health

> ⚠️ L'instance Render Free se met en veille après 15 min d'inactivité.
> Le premier appel déclenche un cold start (~30 s), les suivants sont instantanés.
> La base Neon scale-to-zero après 5 min également (reprise ~500 ms).

## Stack

- **Backend** : Java 17, Spring Boot 4, Spring Data JPA, MapStruct, Lombok
- **Base de données** : PostgreSQL 18 (prod sur Neon), H2 (dev), migrations Flyway
- **Qualité** : JUnit 5, Mockito, MockMvc, AssertJ
- **Infra** : Docker multi-stage, Docker Compose, GitHub Actions CI/CD, GHCR
- **Hébergement** : Render (Web Service) + Neon (Serverless Postgres)
- **Observabilité** : Spring Boot Actuator, SpringDoc OpenAPI (Swagger UI), UptimeRobot

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
- Gestion centralisée des erreurs avec `@RestControllerAdvice` (404 sur ressource
  manquante, 422 sur règle métier violée, 400 sur validation, 500 catch-all)
- Validation Bean Validation (`@Valid`, `@NotBlank`, etc.)
- Migrations de schéma versionnées (V1 → V4)
- State machine sur le cycle de vie des commandes (`OrderStatus`)

## Pipeline de livraison

CI/CD piloté par GitHub Actions, deux workflows complémentaires :

1. **`ci.yml`** - sur chaque push et chaque PR : build Maven, exécution des
   tests unitaires et d'intégration, échec bloquant si la couverture régresse.
2. **`publish.yml`** - sur push `main` validé par la CI : build d'une image
   Docker multi-stage (étape Maven + étape JRE Alpine), publication sur GHCR
   à `ghcr.io/matfen2/foodexpress:latest` et `:sha-<commit>`.
3. **Déploiement Render** - auto-deploy déclenché par push GHCR avec health
   check polling sur `/actuator/health` avant bascule du trafic.

```bash
docker pull ghcr.io/matfen2/foodexpress:latest
```

## Infrastructure

### Hébergement

- **Application** : Render (Web Service Docker, région Frankfurt)
- **Base de données** : Neon Serverless Postgres (tier free, région Frankfurt)
- **Monitoring** : UptimeRobot - check HTTP sur `/actuator/health` toutes les
  5 minutes, alertes email en cas de DOWN > 1 cycle

### Migration de provider DB

Initialement hébergé sur Render Postgres (tier free, suppression automatique
à 30 jours), le projet a été migré vers Neon en juin 2026 pour disposer d'une
base persistante sans limite de durée et pour profiter du modèle serverless
avec scale-to-zero. La migration s'est faite sans modification de code grâce
à l'externalisation complète de `DATABASE_URL`, `DB_USERNAME` et `DB_PASSWORD`
en variables d'environnement Render. Flyway a rejoué automatiquement les
quatre migrations sur la nouvelle base au premier démarrage.

### Variables d'environnement (prod)

| Variable                  | Description                                   |
|---------------------------|-----------------------------------------------|
| `DATABASE_URL`            | URL JDBC complète Neon avec `sslmode=require` |
| `DB_USERNAME`             | Utilisateur Postgres Neon                     |
| `DB_PASSWORD`             | Mot de passe associé                          |
| `SPRING_PROFILES_ACTIVE`  | `prod`                                        |
| `PORT`                    | Assigné dynamiquement par Render              |
