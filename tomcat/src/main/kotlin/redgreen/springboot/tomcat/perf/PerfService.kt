package redgreen.springboot.tomcat.perf

import org.springframework.stereotype.Service
import sample.cpuIntensiveTask

@Service
class PerfService {
    fun performCpuWork(): String {
        return cpuIntensiveTask(1000, 1000).toString()
    }

    fun performIoWork() {
        Thread.sleep(500)
    }
}
