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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PerfController(
    val myService: PerfService
) {
    private val ioExecutor = Executors.newCachedThreadPool()
    private val ioDispatcher = ioExecutor.asCoroutineDispatcher()
    private val fixedExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    @GetMapping("/noop")
    fun noop(): String {
        return "Noop"
    }

    @GetMapping("/suspendNoop")
    suspend fun suspendNoop(): String {
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

    /**
     * this is wrong usage of the Coroutine API. coroutineScope will wait until the nested async is done
     */
    @GetMapping("/deferredIO1")
    suspend fun deferredIO1(): Deferred<String> =
        coroutineScope {
            async(Dispatchers.IO) { // size=max(number of cores, 64)
                myService.performIoWork()
                "deferredIO1"
            }
        }

    /**
     * this is wrong usage of the Coroutine API. coroutineScope will wait until the nested async is done
     */
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

    @OptIn(DelicateCoroutinesApi::class)
    @GetMapping("/deferredIO34")
    fun deferredIO4(): Deferred<String> =
        GlobalScope.async(ioDispatcher) {
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
    fun cpuDirect0(@RequestParam("limit") limit: Int): String {
        return myService.performCpuWork(limit)
    }

    @GetMapping("/cpu0semaphore")
    fun cpuDirect0Sem(@RequestParam("limit") limit: Int): String {
        return myService.performCpuWorkUsingSemaphore(limit)
    }

    @GetMapping("/cpu1")
    suspend fun cpuDirect(@RequestParam("limit") limit: Int): String {
        return myService.performCpuWork(limit)
    }

    @GetMapping("/cpu2")
    suspend fun cpuDispatcher(@RequestParam("limit") limit: Int): String {
        return withContext(Dispatchers.Default) {
            myService.performCpuWork(limit)
        }
    }

    @GetMapping("/cpu3")
    fun cpuThreadpool(@RequestParam("limit") limit: Int): CompletableFuture<String> =
        CompletableFuture.supplyAsync({
            myService.performCpuWork(limit)
        }, fixedExecutor)

    @GetMapping("/cpu4")
    suspend fun cpuCoop(@RequestParam("limit") limit: Int, @RequestParam("batch") batch: Int): String {
        return withContext(Dispatchers.Default) {
            myService.performCpuWorkCoop(limit, batch)
        }
    }
}
