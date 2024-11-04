package sample

import io.github.oshai.kotlinlogging.KotlinLogging

val logger = KotlinLogging.logger {}

fun sayHelloTo(name: String): String {
    logger.info { "saying hello to $name" }
    return "Hello $name"
}
