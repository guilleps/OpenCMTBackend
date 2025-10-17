# OpenCMTBackend

<div align="left">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" height="40" alt="java logo"  />
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" height="40" alt="spring logo"  />
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg" height="40" alt="mysql logo"  />
  <img width="12" />
  <img src="https://cdn.simpleicons.org/hibernate/59666C" height="40" alt="hibernate logo"  />
</div>

###

OpenCMT backend for incident and request management, powering the [user mobile application](https://github.com/guilleps/OpenCMTApp.git) and [admin mobile application](https://github.com/guilleps/OpenCMTAdminApp.git). Developed with Spring Boot 3.3.4 and Java 17, this project provides a secure RESTful API for managing incidents, requests, personnel, vehicles, and sectors, featuring JWT authentication and email notifications.

## 🚀 Key Features

- **Authentication & Authorization**
  - JWT-based Authentication ([java-jwt](https://github.com/auth0/java-jwt))
  - Role-based Access Control (USER, ADMIN) 

- **Incident Management**
  - Incident registration and tracking
  - Incident categorization by type
  - Personnel assignment to incidents
  - PDF report generation

- **Request Management**
  - Request creation and tracking
  - Vehicle assignment
  - Email notifications

- **Administration**
  - User and role management
  - Vehicle administration
  - Sector management

## 🛠️ Technologies Used

- **Backend**
  - Java 17
  - Spring Boot 3.3.4
  - Spring Security
  - Spring Data JPA
  - Hibernate
  - Maven

- **Database**
  - MySQL

- **Security**
  - JWT (JSON Web Tokens)
  - Spring Security

- **Utilities**
  - [Lombok](https://projectlombok.org/)
  - [ModelMapper](https://modelmapper.org/)
  - [Apache PDFBox](https://mvnrepository.com/artifact/org.apache.pdfbox/pdfbox)
  - [Spring Mail](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-mail)

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- MySQL 8.0 or higher
- Git

## 🚀 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/guilleps/OpenCMTBackend.git
   cd OpenCMTBackend
   ```

2. Set up the database:
   - Create a MySQL database named `opencmt`
   - Configure the credentials in `application.properties`

3. Configure email:
   - Update the email settings in `application.properties`

4. Build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. The application will be available at: `http://localhost:8080`

## 📚 API Documentation

API documentation is available in OpenAPI (Swagger) format at:
- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/v3/api-docs`

## 📝 Main Endpoints

### Authentication
- `POST /api/auth/login` - Login and get JWT token
- `POST /api/auth/refresh-token` - Refresh access token

### Incidents
- `GET /api/incidentes` - Get all incidents
- `POST /api/incidentes` - Create a new incident
- `GET /api/incidentes/{id}` - Get incident by ID
- `PUT /api/incidentes/{id}` - Update an incident
- `DELETE /api/incidentes/{id}` - Delete an incident

### Requests
- `GET /api/solicitudes` - Get all requests
- `POST /api/solicitudes` - Create a new request
- `GET /api/solicitudes/{id}` - Get request by ID
- `PUT /api/solicitudes/{id}` - Update a request
- `DELETE /api/solicitudes/{id}` - Delete a request

## 🐳 Docker

You can deploy the application using Docker:

1. Build the image:
   ```bash
   docker build -t opencmt-backend .
   ```

2. Run the container:
   ```bash
   docker run -p 8080:8080 opencmt-backend
   ```

Built with ❤️👽 by the OpenCMT team