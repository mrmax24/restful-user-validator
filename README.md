# RESTful User validator
## Project description:
```
Rest API service for managing information about users, including
registration, full and partial updating, searching by birth dates range and deleting.
```
## Project structure

The application has a Two-Tier Architecture:
- **Presentation layer** (Controller) - accepts requests from clients and sends results back to them.
- **Application logic layer** (Service) - provide logic to operate on the data sent to and from the storage and the client.

## Used technologies and libraries:
| Technology            | Version   |
|:----------------------|:----------|
| `JDK`                 | `17`      |
| `Maven`               | `4.0.0`   |
| `Spring Boot`         | `3.2.5`   |
| `Spring Boot Web`     | `3.2.5`   |
| `Hibernate validator` | `8.0.1`  |
| `Mapstruct`           | `1.5.5`   |
| `Lombok`              | `1.18.32` |
| `Checkstyle`          | `3.3.0`   |
| `JUnit vintage`       | `5.10.2`  |

## Steps to run the program on your computer:
1. Clone the repo: https://github.com/mrmax24/restful-user-validator
2. Run ValidatorRestApiApplication class;
3. Run specific test class;
4. Use postman endpoints to test application
