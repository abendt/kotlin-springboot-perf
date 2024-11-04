package redgreen.springboot.tomcat.perf

import io.opentelemetry.instrumentation.annotations.WithSpan
import kotlin.time.measureTimedValue
import org.springframework.stereotype.Service
import sample.cpuIntensiveTask

@Service
class PerfService {

    @WithSpan("performCpuWork")
    fun performCpuWork(): String {
        return cpuIntensiveTask(1000, 1000).toString()
    }

    @WithSpan("blockingIO")
    fun performIoWork() {
        Thread.sleep(500)
    }
}
