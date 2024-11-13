package redgreen.springboot.webflux.perf

import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@RestController
class PerfController(val perfService: PerfService) {

    val scheduler = Schedulers.fromExecutor(Executors.newCachedThreadPool())

    @GetMapping("/noop")
    fun noop(): String {
        return "noop"
    }

    @GetMapping("/suspendNoop")
    suspend fun suspendNoop(): String {
        return "suspendNoop"
    }

    @GetMapping("/blockingIO")
    fun blockingIO(): String {
        // don't do this!
        perfService.performIoWork()
        return "blockingIO"
    }

    @GetMapping("/monoIO1")
    fun monoIO1(): Mono<String> =
        Mono.fromCallable {
            perfService.performIoWork()
            "monoIO1"
        }.subscribeOn(
            Schedulers.boundedElastic() // size = 10 * numbers of processors
        )

    @GetMapping("/monoIO2")
    fun monoIO2(): Mono<String> =
        Mono.fromCallable {
            perfService.performIoWork()
            "monoIO2"
        }.subscribeOn(scheduler)

    @GetMapping("/dispatcherIO")
    suspend fun dispatcherIO() =
        withContext(Dispatchers.IO) { // size = maxOf(64, number of cores)
            perfService.performIoWork()
            "dispatcherIO"
        }

    @GetMapping("/cpu0")
    fun cpuDirect0(@RequestParam("limit") limit: Int): String {
        return perfService.performCpuWork(limit)
    }

    @GetMapping("/cpu1")
    suspend fun cpuDirect(@RequestParam("limit") limit: Int): String {
        return perfService.performCpuWork(limit)
    }

    @GetMapping("/cpu2")
    suspend fun cpuDispatcher(@RequestParam("limit") limit: Int): String {
        return withContext(Dispatchers.Default) { // size = number of processors
            perfService.performCpuWork(limit)
        }
    }

    @GetMapping("/cpu3")
    fun monoCpu(@RequestParam("limit") limit: Int): Mono<String> {
        return Mono.fromCallable {
            perfService.performCpuWork(limit)
        }
    }

    @GetMapping("/cpu4")
    fun monoCpu2(@RequestParam("limit") limit: Int): Mono<String> {
        return Mono.fromCallable {
            perfService.performCpuWork(limit)
        }.subscribeOn(Schedulers.parallel()) // size = number of processors
    }
}
