# Employee Tracking System API - README

## Overview

The **Employee Tracking System** is a web application designed to manage departments, employees, managers, and projects within an organization. The API provides endpoints for CRUD operations on departments, employees, managers, and projects, ensuring secure access control using **Auth0** for authentication and authorization.

### Features:
- **Department Management**: Create, update, delete, and view departments.
- **Employee Management**: Add, update, delete, and view employees, along with the ability to search employees by attributes like name, department, and designation.
- **Manager Management**: Create, update, delete, and view managers.
- **Project Management**: Create, update, delete, and view projects.
- **Security**: The system uses **Auth0** for user authentication with the **Authorization Code Flow**.

## Authentication & Authorization

The API uses **Auth0** for handling authentication and authorization:

- **OIDC (OpenID Connect)** with **Authorization Code Flow** is used for user authentication.
- Authorization is handled via **roles** and **permissions**:
  - `ROLE_MANAGER`: Access to department and employee management.
  - `ROLE_ADMIN`: Access to manager and project management.
  - `ROLE_EMPLOYEE`: Access to employee/{id}.
  - Custom authorization logic is implemented using `@PreAuthorize` annotations in controller methods.
  - Note: - Admin user will be auto-added to the DB based on applicaiton.properties on application start for accessing other endpoints (Ex. Creating a Manager).

### Steps to Authenticate via Auth0:
1. **Login** using your credentials via Auth0.
2. **Obtain an Access Token** that is used to authenticate requests.

### Security Service:
A custom `SecurityService` is used for role-based access control, ensuring users have proper authorization to access different endpoints.

## Endpoints

### Department Endpoints

1. **GET /departments**
   - Retrieves all departments.
   - **Authorization**: Manager or higher.

2. **GET /departments/{id}**
   - Retrieves a specific department by its ID.
   - **Authorization**: Manager or higher.

3. **POST /departments**
   - Creates a new department.
   - **Authorization**: Manager or higher.

4. **PUT /departments/{id}**
   - Updates an existing department.
   - **Authorization**: Manager or higher.

5. **DELETE /departments/{id}**
   - Deletes a department.
   - **Authorization**: Manager or higher.

6. **GET /departments/{id}/projects**
   - Retrieves the list of projects for a specific department.
   - **Authorization**: Manager or higher.

7. **GET /departments/{deptId}/budget**
   - Retrieves the total budget for a department.
   - **Authorization**: Manager or higher.

### Project Endpoints

1. **GET /projects**
   - Retrieves all projects.
   - **Authorization**: Manager or higher.

2. **GET /projects/{id}**
   - Retrieves a specific project by its ID.
   - **Authorization**: Manager or higher.

3. **POST /projects**
   - Creates a new project.
   - **Authorization**: Manager or higher.

4. **PUT /projects/{id}**
   - Updates an existing project.
   - **Authorization**: Manager or higher.

5. **DELETE /projects/{id}**
   - Deletes a project.
   - **Authorization**: Manager or higher.

6. **GET /projects/{projectId}/employees**
   - Retrieves employees assigned to a specific project.
   - **Authorization**: Manager or higher.

### Manager Endpoints

1. **GET /managers**
   - Retrieves all managers.
   - **Authorization**: Admin.

2. **GET /managers/{id}**
   - Retrieves a specific manager by their ID.
   - **Authorization**: Admin.

3. **POST /managers**
   - Creates a new manager.
   - **Authorization**: Admin.

4. **PUT /managers/{id}**
   - Updates an existing manager.
   - **Authorization**: Admin.

5. **DELETE /managers/{id}**
   - Deletes a manager.
   - **Authorization**: Admin.

### Employee Endpoints

1. **POST /employees**
   - Adds a new employee.
   - **Authorization**: Manager or higher.

2. **GET /employees**
   - Retrieves all employees.
   - **Authorization**: Manager or higher.

3. **GET /employees/{id}**
   - Retrieves an employee by their ID.
   - **Authorization**: Manager(belonging to same department as employee), Employee himself or higher.

4. **PUT /employees/{id}**
   - Updates an existing employee.
   - **Authorization**: Manager or higher.

5. **DELETE /employees/{id}**
   - Deletes an employee.
   - **Authorization**: Manager or higher.

6. **GET /employees/search**
   - Search for employees by name, department, or designation.
   - **Authorization**: Manager or higher.

7. **GET /employees/unassigned**
   - Retrieves employees who are not assigned to any projects.
   - **Authorization**: Manager or higher.

---

## Example Authentication Flow

### 1. **Login with Auth0**:
   - Create Auth0 tenant and create users (Employee, Manager, Admin users added to this application **must have an registered user account** in Auth0)
   - Users log in via Auth0's login page (configured in your `application.properties`).
   - **Authorization URL** : https://yourdomain.auth0.com/authorize?response_type=code&client_id={clientId}&redirect_uri=http://localhost:8081/&scope=openid profile email&audience={audience}
   - **Token URL** : POST - https://yourdomain.auth0.com/oauth/token
   -  **Body** :  {
                      "client_id": "{client_Id}",
                      "client_secret": "{client_secret}",
                      "audience": "https://api.authserver.com",
                      "code": "{Authorization_code}" //received from Authorize endpoint,
                      "grant_type": "authorization_code",
                      "redirect_uri": "http://localhost:8081/"
                      }
   - Upon successful login, Auth0 returns an **Access Token**.

### 2. **Make Authenticated Requests**:
   - Attach the Access Token in the Authorization header as a `Bearer` token when making requests to the API:
     ```bash
     curl -X GET https://api.example.com/departments \
     -H "Authorization: Bearer <access_token>"
     ```

### 3. **Access Control**:
   - Roles such as `ROLE_ADMIN`, `ROLE_MANAGER`, and `ROLE_EMPLOYEE` are assigned to users.
   - Controllers use `@PreAuthorize` to restrict access based on the user's role.
---
## Technologies Used
- **Spring Boot**: Framework for building the backend.
- **Spring Security**: Handles authentication and authorization.
- **Auth0**: Provides authentication via OpenID Connect (OIDC).
- **H2 Database**: In-memory database for development and testing.
- **Lombok**: Used for generating getters, setters, constructors, etc.
- **ModelMapper**: Maps DTOs to Entities and vice versa.
## How to Run
1. **Configure Auth0**:
   - Set up your Auth0 tenant and application.
   - Add the necessary Auth0 configurations (`client_id`, `client_secret`, `issuer`, etc.) in `application.properties`.
2. **Run the Application**:
   ```bash
   ./mvnw spring-boot:run
   ```
3. **Access the API**:
   - The API will be available at `http://localhost:8081`.
4. **Test API Endpoints** using Postman or CURL by passing the Access Token.
---
