# tasker project

This is the home page of the tasker project

# What is the tasker project?

Tasker represents REST service which supports basic CRUD operations.

# What are technologies used?

* Spring Boot 3.0
* PostgreSQL 15.0
* JUnit 5
* [Testcontainers for Java 1.0](https://www.testcontainers.org/)
* Docker
* maven

# How to run tasker locally?

To run the app locally you should perform the following steps:
1. Cloning the repository locally
```
git clone git@github.com:avesanties/RndGif.git
cd ./tasker
```
2. Running maven package phase with `./mvnw clean package -DskipTests`. Flag `-DskipTests` should be specified  
in order to avoid long time waiting of tests containers stopping. 
It's easy to launch tests separately from your favorite IDE or using `./mvnw clean test`.
3. So let's start the app with of [docker compose plugin](https://docs.docker.com/compose/install/linux/)
```
docker compose up
```
>Please keep in mind that directory `/db_init` is mounted into db container under `/docker-entrypoint-initdb.d`  
>to provide initializing SQL script. Therefore docker user must have have sufficient permissions to read and  
>execute in the directory and it's files.

# How to use the app?

You just send a request to http://localhost:8080/tasks.
