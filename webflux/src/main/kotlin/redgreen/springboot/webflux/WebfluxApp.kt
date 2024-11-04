package redgreen.springboot.webflux

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebfluxApp

val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    val processors = Runtime.getRuntime().availableProcessors()

    logger.info { "Using $processors processor(s)" }

    runApplication<WebfluxApp>(*args)
}
