plugins {
    id("module-conventions")

    kotlin("plugin.spring") version "2.0.20"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

dependencies {
    implementation(project(":module"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("io.projectreactor:reactor-test")
}

configureDockerBuildImage("kotlin-springboot-webflux:latest")
