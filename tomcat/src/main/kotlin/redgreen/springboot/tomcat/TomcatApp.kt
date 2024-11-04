package redgreen.springboot.tomcat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TomcatApp

fun main(args: Array<String>) {
    runApplication<TomcatApp>(*args)
}
