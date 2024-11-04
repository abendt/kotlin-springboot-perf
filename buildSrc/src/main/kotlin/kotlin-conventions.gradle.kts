import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    mavenCentral()
}

val libs = the<LibrariesForLibs>()

val isIdea = providers.systemProperty("idea.version").isPresent

tasks.withType(KotlinCompile::class.java).configureEach {
    compilerOptions {
        // don't fail the build when running tests in idea!
        allWarningsAsErrors = !isIdea

        incremental = true
        freeCompilerArgs = listOf("-Xjsr305=strict")

        if (isIdea) {
            freeCompilerArgs.add("-Xdebug")
        }

        jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.get()))
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = libs.versions.java.get()
    targetCompatibility = libs.versions.java.get()
}
