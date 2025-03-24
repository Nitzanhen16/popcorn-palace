# Popcorn Palace Project Instructions

This document provides detailed instructions on how to run, build, and test my Popcorn Palace project.
Follow the steps below to get your development environment set up and to execute the project.

---

## Table of Contents

- [Prerequisites](#prerequisites)
- [Setting Up the Database](#setting-up-the-database)
- [Building the Project](#building-the-project)
- [Running the Project](#running-the-project)
- [Testing the Project](#testing-the-project)
- [Additional Information](#additional-information)

---

## Prerequisites

Before you begin, make sure you have installed the following:

- **Java JDK 21**  
  Ensure you have Java 21 installed. Verify using:

  ```sh
  java -version
  ```

- **Maven**  
  Used for building and testing the project.

  ```sh
  mvn -version
  ```

- **Docker and Docker Compose**  
  These are used to run the PostgreSQL database in a container.

- **Git** (optional)  
  To clone the repository if you haven’t already: https://github.com/Nitzanhen16/popcorn-palace.git

---

## Setting Up the Database

The project uses a PostgreSQL database. You can run a PostgreSQL container using Docker Compose. Follow these steps:

1. **Navigate to the Project Directory**  
   Open a terminal (Command Prompt, PowerShell, or Git Bash) and navigate to the directory containing your `compose.yml` file:

   ```sh
   cd path/to/your/project
   ```

2. **Start the PostgreSQL Container**  
   Run the following command to start the container in detached mode:

   ```sh
   docker-compose up -d
   ```

3. **Check if the Container Is Running**  
   Use the following command to list running containers:

   ```sh
   docker ps
   ```

   You should see your PostgreSQL container listed.

---

## Building the Project

The project uses Maven for build management.

1. **Clean and Build the Project**  
   Open a terminal in your project directory and run:

   ```sh
   mvn clean install
   ```

   This will compile the code, run tests, and package your application.

2. **Note:**
    - Ensure that your Maven dependencies (like `spring-boot-starter-test`, `jackson-databind`, etc.) are properly downloaded.
    - If you encounter any dependency issues, try:

      ```sh
      mvn clean install -U
      ```

---

## Running the Project

After a successful build, you can run the project using the Spring Boot Maven plugin.

1. **Run the Application**  
   In your project directory, execute:

   ```sh
   mvn spring-boot:run
   ```

   The application will start on port **8080** (as specified in `application.yaml`).

2. **Access the Application**  
   Open your browser and navigate to:

   ```
   http://localhost:8080
   ```

---

## Testing the Project

The project includes a test suite for controllers, services, and repositories.

### Running All Tests

To run the complete test suite, execute:

```sh
mvn test
```

### Testing Layers

- **Controller Tests:**  
  These tests simulate HTTP requests using `MockMvc` and verify responses. They use annotations like `@WebMvcTest` and (for now) `@MockBean` for mocking the service layer.

- **Service Tests:**  
  Unit tests for service methods using Mockito `@InjectMocks` and `@Mock` for mocking the repository layer.

- **Repository Tests:**  
  Integration tests using an in-memory H2 database (configured in `application.yaml`). They use annotations like `@DataJpaTest`

---

## Additional Information
please check out **docs** directory, it includes some additional files, that aren't part of the code scope

1. **API documentation**: S JSON collections file that can be imported into Postman, holds the entire API documentation and conveniently enables further testing of my project
2. 1. **roadmap.md**: A document that outlines the planned improvements and features to be added to the project.


---

By following these instructions, you should be able to run, build, and test my Popcorn Palace project successfully.
If you have any questions or run into issues, please refer then to me [here](mailto:nitzan.hen96@gmail.com) further assistance.
