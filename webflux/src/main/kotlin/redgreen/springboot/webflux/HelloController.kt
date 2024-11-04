package redgreen.springboot.webflux

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

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

    @GetMapping("/hello3")
    fun hello3(): Mono<String> {
        return Mono.just("hello3")
    }

}
