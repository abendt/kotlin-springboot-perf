package sample

import kotlin.random.Random

// Function to check if a number is prime
fun isPrime(n: Int): Boolean {
    if (n < 2) return false
    for (i in 2..Math.sqrt(n.toDouble()).toInt()) {
        if (n % i == 0) return false
    }
    return true
}

// Function to calculate a large number of prime numbers
fun calculatePrimes(limit: Int): List<Int> = (2..limit).filter { isPrime(it) }

private val random = Random.Default

// CPU-bound function that simulates heavy computation
fun cpuIntensiveTask(
    iterations: Int,
    primeLimit: Int,
): Long {
    var result = 0L
    var add = true

    for (i in 1..iterations) {
        val primes = calculatePrimes(primeLimit)

        val one = primes[random.nextInt(0, primes.size - 1)]

        if (add) {
            result += one
            add = false
        } else {
            result -= one
            add = true
        }
    }

    return result
}
