plugins {
    id("module-conventions")

    kotlin("plugin.spring") version "2.0.20"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

dependencies {
    implementation(project(":module"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // this is required to enable coroutine support in controllers
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.9.0")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")
}

configureDockerBuildImage("kotlin-springboot-tomcat:latest")
