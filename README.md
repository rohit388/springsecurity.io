# Spring Boot Expense Tracker API

This is a demo project for a Spring Boot application that provides a secure REST API for tracking expenses. It uses Spring Security with JWT for authentication and authorization.

## Features

*   User registration and authentication
*   JWT-based security
*   CRUD operations for expenses
*   Role-based access control
*   Asynchronous expense processing with Apache Kafka
*   API documentation with Swagger

## Technologies Used

*   Java 17
*   Spring Boot 3
*   Spring Security
*   Spring Data JPA
*   Maven
*   MySQL
*   Lombok
*   MapStruct
*   JJWT
*   Spring Kafka
*   SpringDoc OpenAPI (for Swagger UI)

## Getting Started

### Prerequisites

*   JDK 17 or later
*   Maven 3.2+
*   MySQL server running
*   Apache Kafka server running

### Installation

1.  Clone the repository:
    ```sh
    git clone https://github.com/your-username/springsecurity.io.git
    ```
2.  Navigate to the project directory:
    ```sh
    cd springsecurity.io
    ```
3.  Configure the application properties in `src/main/resources/application.properties` with your MySQL database credentials and Kafka server address.
4.  Build the project:
    ```sh
    mvn clean install
    ```
5.  Run the application:
    ```sh
    mvn spring-boot:run
    ```

The application will be running on `http://localhost:8080`.

## API Documentation

Once the application is running, you can access the Swagger UI for API documentation and testing at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Configuration

The main configuration is in the `src/main/resources/application.properties` file.

| Property                          | Description                               | Default Value                  |
| --------------------------------- | ----------------------------------------- | ------------------------------ |
| `spring.datasource.url`           | The connection URL for the MySQL database | `jdbc:mysql://localhost:3306/security1` |
| `spring.datasource.username`      | The username for the database             | `root`                         |
| `spring.datasource.password`      | The password for the database             | `123@Rohit`                    |
| `spring.kafka.bootstrap-servers`  | The address of the Kafka server           | `localhost:9092`               |
| `kafka.topic.expense-events`      | The Kafka topic for expense events        | `expense-events`               |

**Note:** A JWT secret key is used for signing tokens but is not currently defined in the properties file. For production, it is highly recommended to externalize this key.

## API Endpoints

Here is a summary of the available API endpoints:

### Authentication (`/api/auth`)

*   `POST /register`: Register a new user.
*   `POST /login`: Authenticate a user and receive a JWT token.

### Users (`/api/users`)

*   `GET /getAll`: Get a list of all users. (Requires appropriate permissions)
*   `GET /{id}`: Get a specific user by their ID. (Requires appropriate permissions)

### Expenses (`/api/expense`)

*   `POST /create`: Create a new expense. The expense is then sent to a Kafka topic for processing.
