package redgreen.springboot.tomcat.perf

import java.util.concurrent.Semaphore
import kotlinx.coroutines.yield
import org.springframework.stereotype.Service

@Service
class PerfService {

    val semaphore = Semaphore(1)

    fun performCpuWorkUsingSemaphore(limit: Int): String {
        semaphore.acquire()

        try {
            return calculatePrimes(limit).size.toString()
        } finally {
            semaphore.release()
        }
    }

    fun performCpuWork(limit: Int): String {
        return calculatePrimes(limit).size.toString()
    }

    fun performIoWork() {
        Thread.sleep(500)
    }

    fun calculatePrimes(limit: Int): List<Int> {
        val primes = mutableListOf<Int>()
        for (i in 2..limit) {
            var isPrime = true
            for (j in 2..Math.sqrt(i.toDouble()).toInt()) {
                if (i % j == 0) {
                    isPrime = false
                    break
                }
            }
            if (isPrime) primes.add(i)
        }
        return primes
    }

    suspend fun performCpuWorkCoop(limit: Int, batch: Int): String {
        return calculatePrimesCoop(limit, batch).size.toString()
    }

    suspend fun calculatePrimesCoop(limit: Int, batch: Int): List<Int> {
        val primes = mutableListOf<Int>()
        for (i in 2..limit) {
            if (i % batch == 0) yield()

            var isPrime = true
            for (j in 2..Math.sqrt(i.toDouble()).toInt()) {
                if (i % j == 0) {
                    isPrime = false
                    break
                }
            }
            if (isPrime) primes.add(i)
        }
        return primes
    }
}
