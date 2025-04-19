## This is the Mostly complete backend meant to work with:
-https://github.com/StevenScript/concertManagement_Server
De to time retraints, I have not deployed to AWS/Docker. 

## Concert Management Backend

## **Author:** Steven Norris  
## **Date:** April 19, 2025



----

**Author:** Steven Norris  
**Date:** April 19, 2025

## Table of Contents

1. [Introduction](#introduction)
2. [Features](#features)
3. [Architecture](#architecture)
4. [Data Model](#data-model)
5. [Getting Started](#getting-started)
6. [API Endpoints](#api-endpoints)
   - [Authentication](#authentication)
   - [Artists](#artists)
   - [Venues](#venues)
   - [Events](#events)
   - [Tickets](#tickets)
7. [Error Handling](#error-handling)
8. [Security & CORS](#security--cors)

---

## Introduction

The **Concert Management Backend** is a Spring Boot application that exposes a RESTful API for managing concerts, artists, venues, events, tickets, and user accounts. It connects to a relational database (e.g., MySQL) and supports JWT‑based authentication, making it suitable for powering a React frontend.

## Features

- **User Authentication:** Register and login with JWT issuance.
- **Artists:** Create, read, update, list events, ticket counts, and venues for artists.
- **Venues:** Create, read, update, list scheduled artists, upcoming events.
- **Events:** CRUD operations, upcoming events, assign artists, list tickets, ticket counts.
- **Tickets:** Create, read, update tickets by event.
- **Global Exception Handling:** Consistent JSON error responses.
- **Security:** CORS enabled; CSRF disabled; supports HTTP Basic or JWT.

## Architecture

This project follows a layered architecture:

1. **Controller Layer:** Defines REST controllers handling HTTP requests and responses.
2. **Service Layer:** Contains business logic, orchestrates repository calls, and applies rules.
3. **Repository Layer:** Interfaces with the database using Spring Data JPA.
4. **Model & DTOs:** JPA entities represent tables; DTOs and request objects decouple API payloads.
5. **Mapper Components:** Convert between entities and DTOs.

## Data Model

- **Artist**:
   - `id`, `stageName`, `genre`, `membersCount`, `homeCity`
   - Many-to-many with Event

- **Venue**:
   - `id`, `name`, `location`, `capacity`
   - One-to-many with Event

- **Event**:
   - `id`, `eventDate`, `ticketPrice`, `availableTickets`
   - Many-to-one with Venue
   - Many-to-many with Artist
   - One-to-many with Ticket

- **Ticket**:
   - `id`, `seatNumber`, `ticketType`, `buyerName`
   - Many-to-one with Event

- **User**:
   - `id`, `username`, `password`, `role`, `email`

## Getting Started

### Prerequisites

- Java 17 or later
- Maven or Gradle
- MySQL (or other supported datasource)

### Build & Run

1. Clone the repository.
2. Configure `application.properties` with your datasource and JWT properties.
3. Build and run:
   ```bash
   mvn spring-boot:run
   # or
   ./gradlew bootRun
   ```
4. Access the API at `http://localhost:8080`.

## API Endpoints

### Authentication

| Method | URI             | Description                    |
|--------|-----------------|--------------------------------|
| POST   | `/api/register` | Register a new user            |
| POST   | `/api/login`    | Authenticate and receive JWT   |

### Artists

| Method | URI                                 | Description                               |
|--------|-------------------------------------|-------------------------------------------|
| GET    | `/artists`                          | List all artists                          |
| GET    | `/artists/{id}`                     | Get artist by ID                          |
| POST   | `/artists`                          | Create artist                             |
| PUT    | `/artists/{id}`                     | Update artist                             |
| GET    | `/artists/{id}/events`              | List events for an artist                 |
| GET    | `/artists/{id}/ticket-count`        | Total tickets sold for an artist          |
| GET    | `/artists/{id}/venues`              | List venues where an artist performs      |

### Venues

| Method | URI                                     | Description                                 |
|--------|-----------------------------------------|---------------------------------------------|
| GET    | `/venues`                               | List all venues                             |
| GET    | `/venues/{id}`                          | Get venue by ID                             |
| POST   | `/venues`                               | Create venue                                |
| PUT    | `/venues/{id}`                          | Update venue                                |
| GET    | `/venues/{id}/artists`                  | List artists scheduled at a venue           |
| GET    | `/venues/{id}/upcoming-events`          | List upcoming events at a venue             |

### Events

| Method | URI                                          | Description                                  |
|--------|----------------------------------------------|----------------------------------------------|
| GET    | `/events`                                    | List all events                              |
| GET    | `/events/upcoming`                           | List events after today                      |
| GET    | `/events/{id}`                               | Get event by ID                              |
| POST   | `/events`                                    | Create event                                 |
| PUT    | `/events/{id}`                               | Update event                                 |
| GET    | `/events/artist/{artistId}`                  | List events by artist                        |
| POST   | `/events/{eventId}/artists/{artistId}`       | Add artist to an event                       |
| GET    | `/events/{id}/tickets`                      | List tickets for an event                    |
| GET    | `/events/{id}/ticket-count`                 | Count tickets sold for an event              |

### Tickets

| Method | URI                  | Description                  |
|--------|----------------------|------------------------------|
| GET    | `/tickets/{id}`      | Get ticket by ID             |
| POST   | `/tickets`           | Create ticket                |
| PUT    | `/tickets/{id}`      | Update ticket                |

## Error Handling

All errors return HTTP status codes with a JSON body:
```json
{
  "timestamp": "2025-04-19T12:34:56.789",
  "status": 404,
  "error": "Not Found",
  "message": "Detailed error message"
}
```

## Security & CORS

- **CORS**: `*` origins allowed; GET, POST, PUT, DELETE, OPTIONS
- **CSRF**: Disabled
- **Auth**: HTTP Basic or JWT

---
*This documentation reflects the upgraded project structure and endpoints.*

