Here’s your **updated and professional README** for your backend. I’ve included the current features, cleaned the prose for polish, updated security features, and added context about deployment, login throttling, DTO use, etc. Let me know if you'd like this exported as a downloadable PDF as well.

---

````markdown
# 🎤 Concert Management Backend

This is the complete backend for the [Concert Management System](https://github.com/StevenScript/concertManagement_Client), built using Spring Boot. It is designed to integrate with a React frontend and supports full CRUD operations, JWT-based authentication, login throttling, and robust data validation.

**Author:** Steven Norris  
**Last Updated:** June 7, 2025

---

## Table of Contents

1. [Overview](#overview)
2. [Key Features](#key-features)
3. [Architecture](#architecture)
4. [Data Model](#data-model)
5. [Authentication & Security](#authentication--security)
6. [API Endpoints](#api-endpoints)
7. [Error Handling](#error-handling)
8. [Getting Started](#getting-started)
9. [Tech Stack](#tech-stack)

---

## Overview

This backend powers a full-stack concert management platform. It allows administrators to manage artists, venues, events, and tickets, while enabling authenticated users to purchase tickets. Built following clean architecture and REST best practices, the system ensures robust role-based access, efficient querying, and modular growth.

Although fully functional, it is not currently deployed to a public cloud due to time constraints. It has been successfully tested locally using MySQL, Postman, and browser-based UIs.

---

## Key Features

- 🔐 **JWT Authentication** with login throttling and brute-force lockout
- 🧑‍🎤 **Artist Management** with venue and ticket aggregation
- 🏟 **Venue Management** including upcoming event previews
- 🎟 **Event Scheduling** with multi-artist support and ticket linking
- 📄 **Ticket Creation** with buyer details and event mapping
- ⚙️ **Admin-Only Operations** protected via role-based security
- ✅ **DTO & Mapping Layer** to separate internal models from public API
- 🌍 **CORS Support** for frontend integration
- 🧼 **Centralized Error Handling** with clean JSON responses

---

## Architecture

- **Controller Layer** – Exposes RESTful endpoints
- **Service Layer** – Contains business logic and enforces rules
- **Repository Layer** – Uses Spring Data JPA for DB operations
- **Model Layer** – JPA-annotated entities representing core objects
- **DTOs** – Used for cleaner, validated API communication
- **Mappers** – Translate between DTOs and Entities

---

## Data Model

| Entity  | Description |
|---------|-------------|
| **User** | `id`, `username`, `password`, `role`, `email` |
| **Artist** | `id`, `stageName`, `genre`, `membersCount`, `homeCity` |
| **Venue** | `id`, `name`, `location`, `capacity` |
| **Event** | `id`, `eventDate`, `ticketPrice`, `availableTickets` |
| **Ticket** | `id`, `seatNumber`, `ticketType`, `buyerName` |

**Relationships:**

- Artist ↔ Event: Many-to-Many  
- Venue → Event: One-to-Many  
- Event → Ticket: One-to-Many  

---

## Authentication & Security

- ✅ **JWT Issuance:** On login, JWT is generated and sent in the Authorization header
- 🔒 **Login Throttling:** After 3 failed login attempts, user is locked for 15 minutes
- 🧱 **SecurityContext:** Automatically populated by `JwtAuthFilter` for authorized requests
- 🧩 **CORS:** Enabled globally (`*` origin for development)
- ❌ **CSRF:** Disabled for API compatibility

---

## API Endpoints

### 🔐 Authentication

| Method | URI             | Description                    |
|--------|-----------------|--------------------------------|
| POST   | `/api/register` | Register a new user            |
| POST   | `/api/login`    | Authenticate and receive JWT   |

### 🧑‍🎤 Artists

| Method | URI                          | Description                               |
|--------|------------------------------|-------------------------------------------|
| GET    | `/artists`                   | List all artists                          |
| GET    | `/artists/{id}`              | Get artist by ID                          |
| POST   | `/artists`                   | Create new artist                         |
| PUT    | `/artists/{id}`              | Update artist info                        |
| GET    | `/artists/{id}/events`       | List events for an artist                 |
| GET    | `/artists/{id}/ticket-count` | Count of tickets sold across all events   |
| GET    | `/artists/{id}/venues`       | List venues where artist has performed    |

### 🏟 Venues

| Method | URI                             | Description                                |
|--------|---------------------------------|--------------------------------------------|
| GET    | `/venues`                      | List all venues                            |
| GET    | `/venues/{id}`                 | Get venue by ID                            |
| POST   | `/venues`                      | Create new venue                           |
| PUT    | `/venues/{id}`                 | Update venue info                          |
| GET    | `/venues/{id}/artists`         | List scheduled artists                     |
| GET    | `/venues/{id}/upcoming-events` | List upcoming events at venue              |
| GET    | `/venues/hottest?limit=N`     | Top N venues by event count (analytics)    |

### 🎤 Events

| Method | URI                                      | Description                             |
|--------|------------------------------------------|-----------------------------------------|
| GET    | `/events`                                | List all events                         |
| GET    | `/events/upcoming`                       | Events scheduled after today            |
| GET    | `/events/{id}`                           | Get event by ID                         |
| POST   | `/events`                                | Create a new event                      |
| PUT    | `/events/{id}`                           | Update event info                       |
| GET    | `/events/artist/{artistId}`              | List events an artist is performing at  |
| POST   | `/events/{eventId}/artists/{artistId}`   | Add artist to an event                  |
| GET    | `/events/{id}/tickets`                   | Get all tickets for an event            |
| GET    | `/events/{id}/ticket-count`              | Count of tickets sold for this event    |

### 🎟 Tickets

| Method | URI              | Description                 |
|--------|------------------|-----------------------------|
| GET    | `/tickets/{id}`  | Get ticket by ID            |
| POST   | `/tickets`       | Create a new ticket         |
| PUT    | `/tickets/{id}`  | Update ticket info          |

---

## Error Handling

All exceptions are caught and returned with structured JSON responses:

```json
{
  "timestamp": "2025-04-19T12:34:56.789",
  "status": 404,
  "error": "Not Found",
  "message": "Event not found"
}
````

---

## Getting Started

### Prerequisites

* Java 17+
* Maven (or Gradle)
* MySQL (or compatible RDBMS)

### Running Locally

1. **Clone the repo:**

   ```bash
   git clone https://github.com/StevenScript/concertManagement_Server
   cd concertManagement_Server
   ```

2. **Configure your database connection and JWT settings**
   Edit `src/main/resources/application.properties`:

   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/concertdb
   spring.datasource.username=concert
   spring.datasource.password=concertpass
   security.jwt.secret=Your256BitSecretKeyHere
   security.jwt.expiration-ms=86400000
   ```

3. **Start the app:**

   ```bash
   mvn spring-boot:run
   ```

4. **Access**: `http://localhost:8080`

---

## Tech Stack

* **Java 17**
* **Spring Boot 3+**
* **Spring Security (JWT)**
* **Spring Data JPA**
* **MySQL**
* **MapStruct (optional, for DTO mapping)**
* **JUnit + Mockito for testing**

---

## Notes

* 🧪 **Tested** with Postman and frontend integration
* 🐳 Deployment to Docker/AWS planned post-graduation
* 👨‍💻 Designed for learning, but built with production standards
