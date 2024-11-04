package redgreen.springboot.tomcat.perf

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PerfController(
    val myService: PerfService
) {
    private val ioExecutor = Executors.newCachedThreadPool()
    private val ioDispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()
    private val jobExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    @GetMapping("/noop")
    fun noop(): String {
        return "Noop"
    }

    @GetMapping("/suspendNoop")
    suspend fun suspendNoop(): String {
        println(Thread.currentThread().name)

        return "suspendNoop"
    }

    @GetMapping("/blockingIO")
    fun blockingIO(): String {
        myService.performIoWork()
        return "blockingIO"
    }

    @GetMapping("/completableFutureIO")
    fun completableIO(): CompletableFuture<String> {
        return CompletableFuture.supplyAsync({
            myService.performIoWork()
            "completableFutureIO"
        }, ioExecutor)
    }

    @GetMapping("/deferredIO1")
    suspend fun deferredIO1(): Deferred<String> =
        coroutineScope {
            async(Dispatchers.IO) { // size=max(number of cores, 64)
                myService.performIoWork()
                "deferredIO1"
            }
        }


    @GetMapping("/deferredIO2")
    suspend fun deferredIO2(): Deferred<String> =
        coroutineScope {
            async(ioDispatcher) {
                myService.performIoWork()
                "deferredIO2"
            }
        }

    @OptIn(DelicateCoroutinesApi::class)
    @GetMapping("/deferredIO3")
    fun deferredIO3(): Deferred<String> =
        GlobalScope.async {
            myService.performIoWork()
            "deferredIO3"
        }


    @GetMapping("/suspendIO")
    suspend fun supendIO(): String {
        return withContext(Dispatchers.IO) {
            myService.performIoWork()
            "suspendIO"
        }
    }

    @GetMapping("/cpu0")
    fun cpuDirect0(): String {
        return myService.performCpuWork()
    }

    @GetMapping("/cpu1")
    suspend fun cpuDirect(): String {
        return myService.performCpuWork()
    }

    @GetMapping("/cpu2")
    suspend fun cpuDispatcher(): String {
        return withContext(Dispatchers.Default) {
            myService.performCpuWork()
        }
    }

    @GetMapping("/cpu3")
    fun cpuThreadpool(): CompletableFuture<String> =
        CompletableFuture.supplyAsync({
            myService.performCpuWork()
        }, jobExecutor)

}
