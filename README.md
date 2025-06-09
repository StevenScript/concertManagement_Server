# Concert Management Backend

**Author:** Steven Norris
**Date:** April 19, 2025

---

## Table of Contents

1. [Introduction](#introduction)
2. [Features](#features)
3. [Architecture](#architecture)
4. [Data Model](#data-model)
5. [Getting Started](#getting-started)
6. [API Endpoints](#api-endpoints)

   * [Authentication](#authentication)
   * [Users](#users)
   * [Artists](#artists)
   * [Venues](#venues)
   * [Events](#events)
   * [Tickets](#tickets)
7. [Error Handling](#error-handling)
8. [Security and CORS](#security-and-cors)

---

## Introduction

The Concert Management Backend is a Spring Boot application designed to support a React frontend via a RESTful API. It provides secure access to manage users, artists, events, tickets, and venues. Built using a layered architecture, the application emphasizes clean separation of concerns and adheres to testable, production-ready development principles.

## Features

* User Management (Registration, Login, Full Admin CRUD)
* JWT-based authentication with rotating refresh tokens
* Artist, Venue, and Event CRUD with relational integrity
* Ticket generation, association, and validation
* Global error handling with structured JSON
* Secure CORS policy, CSRF disabled
* Deployed-ready configuration with parameterized `.properties`
* Prometheus metrics via Spring Boot Actuator
* Optional OpenTelemetry tracing and structured logging

## Architecture

* **Controller Layer**: Handles REST endpoints
* **Service Layer**: Business logic and validation
* **Repository Layer**: Data persistence using Spring Data JPA
* **Model Layer**: Entity classes that map to DB schema
* **DTOs & Mappers**: Clean separation between entities and API contracts

## Data Model

**User**

* `id`, `username`, `password`, `email`, `role`

**Artist**

* `id`, `stageName`, `genre`, `membersCount`, `homeCity`
* Many-to-many with Event

**Venue**

* `id`, `name`, `location`, `capacity`
* One-to-many with Event

**Event**

* `id`, `eventDate`, `ticketPrice`, `availableTickets`
* Many-to-one with Venue
* Many-to-many with Artist
* One-to-many with Ticket

**Ticket**

* `id`, `seatNumber`, `ticketType`, `buyerName`
* Many-to-one with Event

## Getting Started

### Prerequisites

* Java 17+
* Maven or Gradle
* MySQL (or compatible datasource)

### Setup

1. Clone the repository
2. Configure environment variables or edit `application.properties`
3. Run the backend:

```bash
mvn spring-boot:run
```

Or for Gradle:

```bash
./gradlew bootRun
```

Default API URL: `http://localhost:8080`

## API Endpoints

### Authentication

| Method | Endpoint        | Description                     |
| ------ | --------------- | ------------------------------- |
| POST   | `/api/register` | Register a new user             |
| POST   | `/api/login`    | Authenticate and receive tokens |

### Users (Admin Only)

| Method | Endpoint      | Description                |
| ------ | ------------- | -------------------------- |
| GET    | `/users`      | Retrieve list of all users |
| GET    | `/users/{id}` | Get a single user by ID    |
| POST   | `/users`      | Create a new user          |
| PUT    | `/users/{id}` | Update user details by ID  |
| DELETE | `/users/{id}` | Delete a user by ID        |

### Artists

| Method | Endpoint                     | Description                       |
| ------ | ---------------------------- | --------------------------------- |
| GET    | `/artists`                   | Get all artists                   |
| GET    | `/artists/{id}`              | Get artist by ID                  |
| POST   | `/artists`                   | Create new artist                 |
| PUT    | `/artists/{id}`              | Update artist by ID               |
| GET    | `/artists/{id}/events`       | Events linked to an artist        |
| GET    | `/artists/{id}/venues`       | Venues where artist has performed |
| GET    | `/artists/{id}/ticket-count` | Total tickets sold for an artist  |

### Venues

| Method | Endpoint                       | Description                     |
| ------ | ------------------------------ | ------------------------------- |
| GET    | `/venues`                      | List all venues                 |
| GET    | `/venues/{id}`                 | Get venue by ID                 |
| POST   | `/venues`                      | Create a venue                  |
| PUT    | `/venues/{id}`                 | Update venue details            |
| GET    | `/venues/{id}/artists`         | Artists performing at the venue |
| GET    | `/venues/{id}/upcoming-events` | Future events at this venue     |

### Events

| Method | Endpoint                               | Description                            |
| ------ | -------------------------------------- | -------------------------------------- |
| GET    | `/events`                              | List all events                        |
| GET    | `/events/upcoming`                     | Get events occurring after today       |
| GET    | `/events/{id}`                         | Get event by ID                        |
| POST   | `/events`                              | Create an event                        |
| PUT    | `/events/{id}`                         | Update an event                        |
| GET    | `/events/artist/{artistId}`            | Get events featuring a specific artist |
| POST   | `/events/{eventId}/artists/{artistId}` | Assign artist to event                 |
| GET    | `/events/{id}/tickets`                 | Tickets issued for this event          |
| GET    | `/events/{id}/ticket-count`            | Number of tickets sold for this event  |

### Tickets

| Method | Endpoint        | Description         |
| ------ | --------------- | ------------------- |
| GET    | `/tickets/{id}` | Get ticket by ID    |
| POST   | `/tickets`      | Create ticket       |
| PUT    | `/tickets/{id}` | Update ticket by ID |

## Error Handling

Standard JSON responses for all exceptions:

```json
{
  "timestamp": "2025-04-19T14:30:22.003Z",
  "status": 404,
  "error": "Not Found",
  "message": "User not found"
}
```

Handled globally via a centralized exception handler.

## Security and CORS

* JWT-based access with signed access + refresh tokens
* Token expiration configured via environment or `.properties`
* CORS: Enabled for all origins with safe method constraints (GET, POST, PUT, DELETE, OPTIONS)
* CSRF: Disabled (token-based auth)
* Basic Auth fallback supported (for testing)

## Configuration Overview (application.properties)

* Externalized via environment variables with safe fallbacks
* Supports full deployment pipeline (Docker + AWS Ready)
* Structured logging with `traceId`
* Metrics and tracing ready for Prometheus / OpenTelemetry


