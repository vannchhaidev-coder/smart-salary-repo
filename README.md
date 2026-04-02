# Smart Salary API Help Guide

## Overview

`smart_salary_api` is a Spring Boot backend for salary, payroll, employee finance, wallet, and loan management. The codebase is organized with a standard layered architecture so HTTP requests flow from controller to service to repository, with DTO and mapper layers separating the API contract from persistence models.

Core stack:

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Spring Security with JWT
- PostgreSQL
- MapStruct
- Lombok
- Maven

## Project Structure

Top-level layout:

```text
smart_salary_api/
|- .github/                CI or repository automation
|- .mvn/                   Maven wrapper support files
|- src/
|  |- main/
|  |  |- java/com/vannchhai/smart_salary_api/
|  |  |  |- config/        Application and security configuration
|  |  |  |- controllers/   HTTP endpoints
|  |  |  |- dto/           Request and response payloads
|  |  |  |- enums/         Domain enums
|  |  |  |- exceptions/    Custom exception types
|  |  |  |- mapper/        MapStruct mappers
|  |  |  |- models/        JPA entities
|  |  |  |- repositories/  Spring Data repositories
|  |  |  |- security/      JWT and security helpers/filters
|  |  |  |- seeds/         Seed and bootstrap data
|  |  |  |- services/      Service contracts
|  |  |  |- services/Impl/ Service implementations
|  |  |  |- spaces/        Additional domain-specific grouping
|  |  |  |- SmartSalaryApiApplication.java
|  |  |- resources/
|  |     |- application.yaml
|  |     |- application-dev.yaml
|  |     |- application-prod.yaml
|  |     |- keys/          JWT key material
|  |     |- static/
|  |     |- templates/
|  |- test/                Test sources
|- target/                 Build output
|- mvnw
|- mvnw.cmd
|- pom.xml
|- README.md
|- HELP.md
```

## Main Packages

### `config`

Application-level configuration classes:

- `JpaConfig.java`
- `JwtProperties.java`
- `SecurityConfig.java`

Use this package for cross-cutting framework configuration only. Avoid placing business logic here.

### `controllers`

Entry point for REST APIs and basic web endpoints. Controllers should:

- validate and parse request input
- delegate business logic to services
- return DTO-based responses
- avoid direct repository access

### `dto`

Separated by responsibility:

- `dto/request` for inbound payloads
- `dto/responses` for outbound payloads
- `dto/analytics` for reporting and dashboard projections

This package defines the public API contract. Prefer changing DTOs intentionally and keep entities out of controller responses.

### `models`

Persistence layer entities such as:

- `EmployeeModel`
- `SalaryModel`
- `PayrollModel`
- `LoanModel`
- `LoanPaymentModel`
- `WalletModel`
- `WalletTransactionModel`
- `AttendanceModel`
- `DepartmentModel`
- `PositionModel`
- `UserModel`
- `UserRoleModel`
- `DeductionModel`
- `OvertimeModel`

These classes represent database state and should stay persistence-focused.

### `repositories`

Spring Data JPA repository interfaces used for persistence access, including:

- `EmployeeRepository`
- `SalaryRepository`
- `PayrollRepository`
- `LoanRepository`
- `LoanPaymentRepository`
- `WalletRepository`
- `WalletTransactionRepository`
- `AttendanceRepository`
- `DepartmentRepository`
- `PositionRepository`
- `DeductionRepository`
- `UserRepository`
- `UserRoleRepository`
- `AnalyticsCustomRepository`

Repositories should contain query access patterns, not business orchestration.

### `services` and `services/Impl`

Business logic is defined by service interfaces and implemented in `Impl` classes. Current service areas include:

- authentication
- analytics
- attendance
- employee management
- employee dashboard
- loan management
- loan risk evaluation
- loan transaction handling
- payroll processing
- salary management
- wallet management
- wallet transaction handling

Recommended rule:

- service interface in `services/`
- service implementation in `services/Impl/`
- controller depends on interface, not implementation

### `mapper`

MapStruct mappers convert between entities and DTOs. This is the correct place for transformation logic that would otherwise clutter controllers or services.

### `security`

Security-specific components such as:

- `JwtAuthenticationFilter`
- `JwtService`
- `CustomUserDetailsService`
- `SecurityUtil`
- request logging and timing filters

Keep authentication and authorization concerns here rather than scattering them across business services.

### `exceptions`

Custom exceptions for domain and request failures. Pair these with centralized exception handling if you extend the API further.

### `seeds`

Bootstrap data classes for departments, employees, salaries, payroll, loans, wallets, attendance, and related entities. Use this package for controlled startup data, not for long-term migration management.

## Request Flow

Standard request lifecycle in this project:

1. A controller receives the HTTP request.
2. Input is mapped into a request DTO.
3. The controller calls a service interface.
4. The service implementation enforces business rules.
5. Repositories load or persist entities.
6. Mappers convert entities into response DTOs.
7. The controller returns the response.

This separation should be preserved when adding new features.

## Configuration Files

Environment configuration is located in:

- `src/main/resources/application.yaml`
- `src/main/resources/application-dev.yaml`
- `src/main/resources/application-prod.yaml`

Use:

- `application.yaml` for shared defaults
- `application-dev.yaml` for local development overrides
- `application-prod.yaml` for production-specific configuration

JWT key files are stored under `src/main/resources/keys/`.

## Build and Run

Common commands:

```bash
./mvnw spring-boot:run
./mvnw test
./mvnw clean package
./mvnw fmt:check
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
.\mvnw.cmd test
.\mvnw.cmd clean package
.\mvnw.cmd fmt:check
```

## Development Guidelines

### Add a new feature

When introducing a new module, follow this sequence:

1. Create or update the entity in `models/`.
2. Add the repository in `repositories/`.
3. Define request and response DTOs in `dto/`.
4. Add or update the mapper in `mapper/`.
5. Define the service interface in `services/`.
6. Implement the business logic in `services/Impl/`.
7. Expose the endpoint in `controllers/`.
8. Add tests under `src/test/`.

### Keep responsibilities clean

- Controllers handle HTTP concerns.
- Services handle business rules.
- Repositories handle database access.
- Mappers handle object transformation.
- Entities model persistence.
- DTOs model API payloads.

### Security rules

- Route protection belongs in Spring Security configuration and JWT filters.
- Avoid embedding role checks directly in controllers when framework-level authorization is more appropriate.
- Never expose sensitive internal fields from entity classes directly.

### Documentation rules

- Keep this file focused on project structure, workflow, and maintenance guidance.
- Put endpoint-level examples in a dedicated API reference document if the project grows further.
- Update this file when package layout or architectural conventions change.

## Notes

- `pom.xml` currently includes Jib for container image builds and `fmt-maven-plugin` for formatting checks.
- `target/` is generated output and should not be edited manually.
- The root-level `private.pem` should be handled carefully and reviewed against project security practices.
