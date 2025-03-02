# Building the Java Server (Spring Boot REST API)
## Author - Steven Norris
## Date - February 28th, 2025 

# Concert Management Server

## Table of Contents

1. [Introduction](#introduction)
2. [Features](#features)
3. [Architecture](#architecture)
4. [Data Model](#data-model)
5. [API Endpoints](#api-endpoints)
    - [Artist Endpoints](#artist-endpoints)
    - [Venue Endpoints](#venue-endpoints)
    - [Event Endpoints](#event-endpoints)
    - [Ticket Endpoints](#ticket-endpoints)

## Introduction

The Concert Management Server is a Java-based application developed to manage concerts, artists, venues, events, and ticketing. Built using Spring Boot, it exposes a RESTful API over HTTP and interacts with a MySQL relational database to perform various operations related to concert management.

## Features

- **CRUD Operations**: Manage artists, venues, events, and tickets through standard Create, Read, Update, and Delete operations.
- **Custom Queries**: Retrieve specific information such as events for a particular artist, venues hosting certain events, and more.
- **Client Integration**: Designed to work seamlessly with a separate Java console client that communicates via HTTP with the API.

## Architecture

The application follows a layered architecture:

1. **Controller Layer**: Handles HTTP requests and responses.
2. **Service Layer**: Contains business logic and interacts with the repository layer.
3. **Repository Layer**: Interfaces with the MySQL database using Spring Data JPA.

## Data Model

The primary entities and their relationships are as follows:

- **Artist**: Represents performers and has a many-to-many relationship with events.
- **Venue**: Represents locations where events are held and has a one-to-many relationship with events.
- **Event**: Represents concerts or performances and has one-to-many relationships with tickets.
- **Ticket**: Represents tickets for events.

## API Endpoints

The REST API provides the following endpoints:

### Artist Endpoints

- `GET /artists`: Retrieve a list of all artists.
- `POST /artists`: Create a new artist.
- `GET /artists/{id}`: Retrieve details of a specific artist by ID.
- `PUT /artists/{id}`: Update an existing artist by ID.
- `DELETE /artists/{id}`: Delete an artist by ID.

### Venue Endpoints

- `GET /venues`: Retrieve a list of all venues.
- `POST /venues`: Create a new venue.
- `GET /venues/{id}`: Retrieve details of a specific venue by ID.
- `PUT /venues/{id}`: Update an existing venue by ID.
- `DELETE /venues/{id}`: Delete a venue by ID.

### Event Endpoints

- `GET /events`: Retrieve a list of all events.
- `POST /events`: Create a new event.
- `GET /events/{id}`: Retrieve details of a specific event by ID.
- `PUT /events/{id}`: Update an existing event by ID.
- `DELETE /events/{id}`: Delete an event by ID.

### Ticket Endpoints

- `GET /tickets`: Retrieve a list of all tickets.
- `POST /tickets`: Create a new ticket.
- `GET /tickets/{id}`: Retrieve details of a specific ticket by ID.
- `PUT /tickets/{id}`: Update an existing ticket by ID.
- `DELETE /tickets/{id}`: Delete a ticket by ID.
