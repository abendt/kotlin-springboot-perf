plugins {
    id("kotlin-conventions")

    alias(libs.plugins.gatling) version libs.versions.gatlingPlugin
}

dependencies {
 //   testImplementation(libs.gatling.charts)
  //  testImplementation(libs.gatling.core)

    gatlingImplementation("io.rest-assured:rest-assured:5.5.0")
}

gatling {
}
