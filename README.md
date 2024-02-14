![Docker](https://github.com/DRadman/loghub-backend/actions/workflows/docker-publish.yml/badge.svg?branch=master)
[![GitHub Latest Release)](https://img.shields.io/github/v/release/DRadman/loghub-backend?logo=github)](https://github.com/DRadman/loghub-backend/releases)
[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](LICENSE)
[![codecov](https://codecov.io/gh/DRadman/loghub-backend/graph/badge.svg?token=TJV9QZPTNR)](https://codecov.io/gh/DRadman/loghub-backend)
![Github All Releases](https://img.shields.io/github/downloads/DRadman/loghub-backend/total.svg?style=flat&logo=github)

[![Open Source](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://opensource.org/)

[![Issues](https://img.shields.io/github/issues-raw/DRadman/loghub-backend.svg?style=flat&logo=github)](https://github.com/Dradman/loghub-backend/issues)
[![GitHub pull requests](https://img.shields.io/github/issues-pr/DRadman/loghub-backend.svg?style=flat&logo=github)](https://github.com/Dradman/loghub-backend/pulls)
![GitHub contributors](https://img.shields.io/github/contributors/DRadman/loghub-backend.svg?style=flat&logo=github)
![GitHub last commit](https://img.shields.io/github/last-commit/DRadman/loghub-backend.svg?style=flat&logo=github)


# LogHub - Remote Centralized Logging Solution (Backend)
This project is a Spring Boot backend application that provides a remote centralized logging solution. It allows you to collect and manage logs from distributed applications in a centralized location, enhancing troubleshooting and monitoring capabilities.

## Technologies Used
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=whit)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)
![Git](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

## Features
- Centralized logging from distributed applications
- Integration with MinIO for file storage
- Integration with Mailjet for sending emails
- MongoDB for database storage
- Swagger UI for API documentation

## Building the Application
### Prerequisites
- Gradle
- JDK 21

```bash
./gradlew assemble
```

## Starting the Application
### Prerequisites
- Gradle
- JDK 21
- Docker

To run application you will need to startup database & minio database first.
Boot run command will do that automatically for you.

```bash
./gradlew bootRun
```

## Deploying with Docker

You can deploy the application using Docker. First, pull the latest Docker image from the GitHub Container Registry:
```bash
docker pull ghcr.io/dradman/loghub-backend:latest
```

Then, run the Docker container:
```bash
docker run -p 8080:8080 ghcr.io/dradman/loghub-backend:latest
```

***Important:*** Make sure that MongoDB & MinIO service is running on your machine & all environment variables are set correctly, otherwise application will crash

### Docker-Compose

You can also run application using docker compose. See an example [here](docs/examples/docker-compose.yaml)

## Accessing Initial User (Database Seeding)
Application by the default will create admin user for initial access. It's recommended to change his password, and account details.

```
username: admin@loghub.com
password: admin123
```

***Notice:*** This user can't be deleted since he is superadmin and maintainer of the software 

## Environment Variables

| Name                            | Description                                             | Default Value         | Required                  |
|---------------------------------|---------------------------------------------------------|-----------------------|---------------------------|
| SERVER_PORT                     | Port on which application will run                      | 8080                  | No                        |
| SERVER_HOST                     | Host on which application will run                      | http://localhost:8080 | No, recommended to change |
| ENVIRONMENT                     | Application Environment                                 | local                 | No                        |
| CMS_URL                         | Url Of the CMS and where is running                     | http://localhost:4200 | No, recommended to change |
| ENABLE_REGISTRATION             | Whether Registration of the new users is enabled or not | true                  | No                        |
| APP_NAME                        | Name of the application                                 | LogHub                | No                        |
| DB_HOST                         | Hostname of the database                                | localhost             | No, recommended to change |
| DB_PORT                         | Port of the database                                    | 27017                 | No                        |
| DB_USERNAME                     | Username to access database                             | loghub                | No                        |
| DB_PASSWORD                     | Password to access database                             | xps*NI_mYJr6PT\*      | No, recommended to change |
| JWT_KEY                         | Json Web Token Signing Key                              | superSecretKey        | No, recommended to change |
| TOKEN_DURATION                  | Duration of the JWT access token in seconds             | 3600 (1 Hour)         | No                        |
| REFRESH_TOKEN_DURATION          | Duration of the JWT refresh token in seconds            | 86400 (24 Hour)       | No                        |
| SWAGGER_UI_PATH                 | Path for the SwaggerUI documentation                    | /swagger-ui.html      | No                        |
| API_DOCS_PATH                   | Path for the OpenApi docs JSON Schema                   | /api-docs             | No                        |
| MAILJET_ACCESS_KEY              | Access key for the MailJet Account                      | mailjetAccessKey      | Yes                       |
| MAILJET_SECRET_KEY              | Secret key for the MailJet Account                      | mailjetSecretKey      | Yes                       |
| MAILJET_SENDER_EMAIL            | Sender email for the MailJet Account                    | no-reply@decodex.net  | No, recommended to change |
| MAILJET_INVITATION_TEMPLATE     | MailJet template id for the invitation email            | 5679542               | No                        |
| MAILJET_RESET_PASSWORD_TEMPLATE | MailJet template id for reset password email            | 5681472               | No                        |
| MINIO_ACCESS_KEY                | MinIO Access Key                                        | minioAccessKey        | Yes                       |
| MINIO_SECRET_KEY                | MinIO Secret Key                                        | minioSecretKey        | Yes                       |
| MINIO_BUCKET                    | Name of the minio bucket                                | loghub-bucket         | No                        |
| MINIO_CHECK_BUCKET              | Checks whether minio bucket exists                      | true                  | No                        |
| MINIO_CREATE_BUCKET             | Creates new minio bucket if it doesn't exist            | true                  | No                        |
| MINIO_CONNECT_TIMEOUT           | Timeout period in seconds to connect to minio           | 10                    | No                        |
| MINIO_READ_TIMEOUT              | Timeout period in seconds to read file from MinIO       | 10                    | No                        |
| MINIO_WRITE_TIMEOUT             | Timeout period in seconds to write file to MinIO        | 60                    | No                        |
| MAX_FILE_SIZE                   | Maximum file size which is to be uploaded               | 20MB                  | No                        |
| MAX_REQUEST_SIZE                | Maximum request size                                    | 20MB                  | No                        |
| FILE_SIZE_THRESHOLD             | Multipart file size threshold                           | 2KB                   | No                        |


![Docker](https://github.com/DRadman/loghub-cms/actions/workflows/docker-publish.yml/badge.svg?branch=master)
[![GitHub Latest Release)](https://img.shields.io/github/v/release/DRadman/loghub-cms?logo=github)](https://github.com/DRadman/loghub-cms/releases)
[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](LICENSE)
[![codecov](https://codecov.io/gh/DRadman/loghub-cms/graph/badge.svg?token=TJV9QZPTNR)](https://codecov.io/gh/DRadman/loghub-cms)
![Github All Releases](https://img.shields.io/github/downloads/DRadman/loghub-cms/total.svg?style=flat&logo=github)

[![Open Source](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://opensource.org/)

[![Issues](https://img.shields.io/github/issues-raw/DRadman/loghub-cms.svg?style=flat&logo=github)](https://github.com/Dradman/loghub-cms/issues)
[![GitHub pull requests](https://img.shields.io/github/issues-pr/DRadman/loghub-cms.svg?style=flat&logo=github)](https://github.com/Dradman/loghub-cms/pulls)
![GitHub contributors](https://img.shields.io/github/contributors/DRadman/loghub-cms.svg?style=flat&logo=github)
![GitHub last commit](https://img.shields.io/github/last-commit/DRadman/loghub-cms.svg?style=flat&logo=github)


# LogHub - Remote Centralized Logging Solution (CMS)
This project is a Angular CMS application that provides an UI for LogHub (remote centralized logging solution). It allows you to collect and manage logs from distributed applications in a centralized location, enhancing troubleshooting and monitoring capabilities.

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 17.1.3.

This project utilizes next features of angular:
- Angular 17
- SSR (Server Side Rendering)
- Standalone Components
- Router (For url routing)
- HTTP Client (For api calls)
- NGRX (For state management)
- PrimeNG (For UI Components)


## Technologies Used
![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML-239120?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS-239120?&style=for-the-badge&logo=css3&logoColor=white)
![Pretier](https://img.shields.io/badge/prettier-1A2C34?style=for-the-badge&logo=prettier&logoColor=F7BA3E)
![Jest](https://img.shields.io/badge/Jest-323330?style=for-the-badge&logo=Jest&logoColor=white)
![Node](https://img.shields.io/badge/Node.js-43853D?style=for-the-badge&logo=node.js&logoColor=white)
![Vercel](https://img.shields.io/badge/Vercel-000000?style=for-the-badge&logo=vercel&logoColor=white)
![Git](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via a platform of your choice. To use this command, you need to first add a package that implements end-to-end testing capabilities.

## Deploying with Docker

You can deploy the application using Docker. First, pull the latest Docker image from the GitHub Container Registry:
```bash
docker pull ghcr.io/dradman/loghub-cms:latest
```

Then, run the Docker container:
```bash
docker run -p 80:4000 ghcr.io/dradman/loghub-cms:latest
```

***Important:*** Make sure that all environment variables are set correctly.

### Docker-Compose

You can also run application using docker compose. See an example [here](docs/examples/docker-compose.yaml)

## Environment Variables

***Important:*** Due to angular & general limitation since application is served to local browser you can't use standard .env to update environment varaibles.
If you wish to change any of the variables you ***will have to*** build your own version of application where will change [these files](src/environments/)


You can also build your own 