Kotlin springboot blueprint
===========================

* Spring Tomcat
* Spring WebFlux
  * uses Netty

# First steps

## Build & run all tests

```shell
./gradlew build
```

# Running the service

Starts Spring boot app in docker (docker container is hot reloaded when you make changes to the source code)

## Spring Boot Tomcat

* http://localhost:8080

```shell
cd tomcat
./start-tomcat.sh
```

## Spring Boot Webflux
* http://localhost:8081

```shell
cd webflux
./start-webflux.sh
```

# Run load tests

Start the respective server first!

Settings:
* 12 threads
* 400 connections
* 30 seconds

## Spring Boot Tomcat

```shell
cd tomcat
./tomcat-load.sh
```

## Spring Boot Webflux

```shell
cd webflux
./webflux-load.sh
```

