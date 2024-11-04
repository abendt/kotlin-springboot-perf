plugins {
    `kotlin-dsl`
    id("com.diffplug.spotless") version libs.versions.gradleSpotlessPlugin.get()
}

repositories {
    gradlePluginPortal()
}

dependencies {
    val kotlinVersion = libs.versions.kotlin.get()
    val testLoggerPluginVersion = libs.versions.gradleTestLoggerPlugin.get()
    val spotlessVersion = libs.versions.gradleSpotlessPlugin.get()

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("com.adarshr:gradle-test-logger-plugin:$testLoggerPluginVersion")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:$spotlessVersion")
    implementation("org.jetbrains.kotlinx:kover-gradle-plugin:0.8.3")
    implementation("com.bmuschko:gradle-docker-plugin:9.4.0")

    // workaround to be able to use the version catalog inside of gradle convention plugins
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

spotless {
    ratchetFrom("HEAD")

    kotlin {
        ktlint()
        targetExclude("build/generated-sources/**")
    }

    kotlinGradle {
        ktlint()
    }
}
