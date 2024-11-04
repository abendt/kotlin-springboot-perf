package redgreen.springboot.tomcat

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @GetMapping("/hello1")
    fun hello1(): String {
        return "hello1"
    }

    @GetMapping("/hello2")
    suspend fun hello2(): String {
        return "hello2"
    }
}
