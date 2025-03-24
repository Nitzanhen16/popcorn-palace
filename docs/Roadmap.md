# Project Roadmap

This document outlines the planned improvements and features to be added to the project. These updates are aimed at enhancing functionality, maintaining code quality, and ensuring long-term scalability.

---

## 1. Refactor Showtime API

- **Objective**: Simplify and enhance the Showtime API by making it more efficient and aligned with future needs.
- **Changes**:
    - Replace the `theater_name` field with `theater_id` to improve consistency and avoid issues with name mismatches.
    - Eliminate the need to send the `endtime` of the showtime, as this can be calculated using the movie’s duration.
- **Implementation Details**:
    - Update the API endpoints to reflect the change in the payload (use `theater_id` instead of `theater_name`).
    - Modify the service layer to automatically calculate the `endtime` based on the movie's `duration`.

## 2. Create Theater and User Entities for Future Expansions

- **Objective**: Lay the foundation for future features by creating `Theater` and `User` entities.
- **Implementation Details**:
    - **Theater Entity**: This will be used to store information about theaters, such as their names, locations, and associated showtimes.
    - **User Entity**: This will serve as the foundation for implementing user-related features, such as user accounts, roles, and preferences in the future.
    - Both entities should include common fields such as `create_date`, `write_date`, and `active`.

## 3. Expand CRUD Operations for All Services

- **Objective**: Implement full Create, Read, Update, and Delete (CRUD) operations for all services across the application.
- **Implementation Details**:
    - Extend existing service layers to support all CRUD operations.
    - Ensure that each entity is properly handled with the corresponding HTTP methods in the controller layer.
    - Review and optimize the current implementation to ensure that all business logic is correctly applied during each operation.

## 4. Add Metadata Fields for All Entities

- **Objective**: Introduce new fields to track entity creation and modification, and manage active status.
- **Fields to Add**:
    - `create_date`: Timestamp when the entity is first created.
    - `write_date`: Timestamp when the entity is last modified.
    - `active`: Boolean flag indicating whether the entity is active or inactive (to replace soft deletes).
- **Implementation Details**:
    - Update the database schema and modify entity models to incorporate these new fields.
    - Ensure that both the service and controller layers correctly handle these fields during create, update, and query operations.
    - Implement logic to handle the `active` status instead of deletion.

## 5. Add Logging

- **Objective**: Integrate logging functionality to capture essential information and facilitate debugging and monitoring.
- **Implementation Details**:
    - Use a logging framework (e.g., Logback, SLF4J) for consistent and structured logging throughout the application.
    - Log key events, such as API request and response data, service layer actions, and error handling.
    - Implement different log levels (e.g., DEBUG, INFO, WARN, ERROR) based on the nature of the event.
    - Ensure log messages provide enough context without compromising security (e.g., avoid logging sensitive data).

## 6. Ensure High Test Coverage (>80%)

- **Objective**: Achieve greater than 80% test coverage across the entire application to ensure code reliability and maintainability.
- **Implementation Details**:
    - Utilize test coverage tools like **SonarQube** and **JaCoCo** to track and measure test coverage.
    - Write unit, integration, and acceptance tests to cover key components, including services, repositories, and controllers.
    - Ensure that tests are comprehensive and cover edge cases and error handling.
    - Continuously monitor and improve test coverage as part of the development process.

## 7. Create a Model Mapping Layer

- **Objective**: Introduce a dedicated layer for mapping between the service and controller layers to improve code organization and maintainability.
- **Implementation Details**:
    - Implement a model mapping layer (e.g., using libraries like MapStruct or ModelMapper) to handle the conversion between service and API model objects.
    - Refactor existing code to integrate the new mapping layer while ensuring minimal impact on the current functionality.
    - Ensure that the mapping logic is centralized and reusable, reducing redundancy and improving testability.

## 8. Add End-to-End Testing

- **Objective**: Implement end-to-end (E2E) tests to ensure the entire application works as expected, from the API layer down to the database.
- **Implementation Details**:
    - Write E2E tests to simulate real user workflows and validate the interaction between the front-end, back-end, and database layers.
    - Use tools like **Cypress**, **Selenium**, or **Testcontainers** to facilitate the testing of the entire application stack.
    - Ensure that E2E tests cover critical paths and validate core functionality (e.g., user registration, authentication, CRUD operations).

---

## Conclusion

This roadmap outlines the next set of priorities for enhancing the project. By addressing these goals, we aim to improve the application's functionality, code quality, and long-term maintainability. Continuous testing and refactoring will ensure that the system remains robust and scalable as new features are added.
