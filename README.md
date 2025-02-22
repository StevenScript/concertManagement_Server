# Building the Java Server (Spring Boot REST API)
## Author - Steven Norris
## Date - February 28th, 2025 

### Entities
- **Artist**
- **Venue**
- **Event**
- **Ticket**

### Relationships
- **Artist ↔ Event**: Many-to-Many
- **Venue ↔ Event**: One-to-Many
- **Event ↔ Ticket**: One-to-Many

### Features
- A **REST API** using **Spring Boot** for CRUD operations and custom queries (e.g., *"list events for an artist"*).
- A separate **Java console client** that communicates via HTTP with the API.

### Workflow
- **Trunk-based development** with feature branches and pull requests.
- **Test-driven Development** with testing and github workplows to ensure stable code and catching bugs

## Project Setup

### Dependencies (via Spring Initializr)
- **Spring Web**
- **Spring Data JPA**
- **MySQL Driver**
- **Lombok**
- **Spring Boot DevTools**

## Planned Steps
1. **Project Setup and GitHub Initialization**
2. **Define the Data Model**
3. **Create Repository Interfaces**
4. **Develop REST Controllers**
5. **Test API via Postman**

---
