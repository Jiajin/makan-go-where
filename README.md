# makan-go-where

makan-go-where (MGW) is a REST API that serves as the backend for an application that allows users to invite their friends to sessions and randomize their next meal location.

## Setup

1. Clone the repository.
2. Install Java 17 with `brew` or via your own preferred method

```shell
brew install openjdk@17
java --version # output should be openjdk 17.0.9 2023-10-17
```

3. Install maven with `brew` or via your own preferred method

```shell
brew install maven
mvn -version #output should be Apache Maven 3.9.6 (bc0240f3c744dd6b6ec2920b3cd08dcc295161ae)
```

4. Run `mvn clean install` to build the project.

5. Install and create a postgres database, updating `application.properties` with url/username/password

```shell
brew install postgresql
```

6. Run `mvn spring-boot:run` to start the application.

## Documentation

### Swagger UI

After runniing the application, you can access the swagger documentation at:

```
http://localhost:8080/swagger-ui/index.html#/
http://localhost:8080/api-docs
```

### Postman Collection

The import json file for Postman can be found in `/docs`

## Suggested application flow

### Login

- `/getPerson` to retrieve User details and list of active sessions by email
- `/savePerson` to create a new account

### Homepage

- `/saveMeeting` to create a new session

### Meeting page

- `/getMeetingById` to load or refresh current Session and list of submitted locations
- `/acceptInvite` to send invites to other users to join the session
- `/createPlace` to create/update meal location for the session
- `/finalizeMeeting` to close the session and select the meal location for the session

## Design/Architectural Decisions

### Database

- Opted to use Springboot JPA/Hibenate to generate the tables for faster initial development.
- Recommended to adopt migrations using Flyway or your preferred migration tool

### Identity fields

- The Frontend application should adopt a Token or Login Session for populating the relevant IDs required for the REST API calls in this REST API
