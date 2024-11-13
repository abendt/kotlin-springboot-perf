package redgreen.springboot.webflux.perf

import org.springframework.stereotype.Service

@Service
class PerfService {

    fun performIoWork() {
        Thread.sleep(500)
    }

    fun performCpuWork(limit: Int): String {
        return calculatePrimes(limit).sum().toString()
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
}
