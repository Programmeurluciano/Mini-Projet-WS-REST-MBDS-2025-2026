
#  Movie API - Mini Projet WS REST

API REST de gestion de films avec authentification JWT, rôles (USER / ADMIN), recherche avancée et documentation Swagger.

**Master MBDS - 2025/2026** | **ETU1909 Hasina ETU0953 Luciano** | **30 mars 2026**

---

#  Présentation

Cette API REST permet de gérer :

- les films
- les acteurs
- les catégories
- les utilisateurs
- les avis (reviews)

Elle inclut :

- authentification **JWT**
- gestion des **rôles**
- **recherche avancée**
- **pagination et filtrage**
- documentation **Swagger / OpenAPI**

---

#  Stack Technique

| Technologie | Version |
|-------------|--------|
| Java | 21 |
| Spring Boot | 4.0.3 |
| Spring Security | - |
| JWT | 0.11.5 |
| H2 Database | - |
| SpringDoc OpenAPI | 2.2.0 |
| Lombok | - |

---

#  Modèle de Données

| Entité | Relations |
|------|------|
| **User** | 1..n Reviews, 1..n RefreshToken, 1..1 VerificationToken |
| **Movie** | n..n Categories, n..n Actors, 1..n Reviews |
| **Category** | n..n Movies |
| **Actor** | n..n Movies |
| **Review** | n..1 User, n..1 Movie |
| **RefreshToken** | n..1 User |
| **VerificationToken** | 1..1 User |
| **Role** | 1..n User |

---

#  Endpoints API

##  Auth (`/api/auth`)

| Méthode | Endpoint | Accès |
|------|------|------|
| POST | `/register` | Public |
| POST | `/login` | Public |
| POST | `/refresh` | Public |
| POST | `/logout` | Authentifié |

---

##  User (`/api/users`)

| Méthode | Endpoint | Accès |
|------|------|------|
| GET | `/` | ADMIN |
| GET | `/{id}` | Authentifié |
| PUT | `/{id}` | Authentifié |
| DELETE | `/{id}` | ADMIN |

---

##  Movie (`/api/movies`)

| Méthode | Endpoint | Accès |
|------|------|------|
| GET | `/{id}` | Public |
| POST | `/{id}/upload-media` | ADMIN |
| POST | `/{id}/manage-ranking` | ADMIN |
| PUT | `/update/{year}` | ADMIN |
| GET | `/tag-enabled` | Public |
| GET | `/search` | Public |
| GET | `/category/{categoryName}` | Public |
| PUT | `/edit/{id}` | ADMIN |

---

##  Category (`/api/categories`)

| Méthode | Endpoint | Accès |
|------|------|------|
| GET | `/{id}` | Public |
| POST | `/` | ADMIN |
| PUT | `/{id}` | ADMIN |
| DELETE | `/{id}` | ADMIN |

---

##  Actor (`/api/actors`)

| Méthode | Endpoint | Accès |
|------|------|------|
| GET | `/{id}` | Public |
| POST | `/` | ADMIN |
| PUT | `/{id}` | ADMIN |
| DELETE | `/{id}` | ADMIN |

---

##  Review (`/api/reviews`)

| Méthode | Endpoint | Accès |
|------|------|------|
| GET | `/{id}` | Public |
| POST | `/` | Authentifié |
| PUT | `/{id}` | Authentifié |
| DELETE | `/{id}` | Authentifié ou ADMIN |

---

#  Sécurité

- **JWT Access Token** : 15 minutes
- **Refresh Token** : 7 jours
- **Header** : `Authorization: Bearer <token>`
- **Rôles** : `ROLE_USER` (lecture + avis) / `ROLE_ADMIN` (tout)

```

Authorization: Bearer <token>

````

- **Rôles**

| Rôle | Permissions |
|-----|-----|
| ROLE_USER | Lecture + ajout d'avis |
| ROLE_ADMIN | Accès complet |

---

#  Fonctionnalités Avancées

-  **Recherche dynamique**
  - titres
  - années
  - catégories
  - acteurs
  - notes

-  **Agrégations**
  - classement des films
  - comptage par catégorie

-  **Filtrage**
  - JPA Specifications
  - pagination

-  **HATEOAS**
  - liens hypermédias

-  **Validation email**
  - templates Thymeleaf

---

#  Configuration

## `application.properties`

```properties
spring.application.name=wsrest

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.datasource.url=jdbc:h2:mem:film_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true

spring.mail.host=smtp.email.com
spring.mail.port=2525
spring.mail.username=test@gmail.com
spring.mail.password=password

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
````

---

# Installation

```bash
git clone <repository-url>
cd spring-boot-movie-api-main

mvn clean install
mvn spring-boot:run
```

---

# Accès à l'application

| Service    | URL                                                                            |
| ---------- | ------------------------------------------------------------------------------ |
| API        | [http://localhost:8080](http://localhost:8080)                                 |
| H2 Console | [http://localhost:8080/h2-console](http://localhost:8080/h2-console)           |
| Swagger UI | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) |

---
