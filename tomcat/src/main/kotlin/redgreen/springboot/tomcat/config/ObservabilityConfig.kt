package redgreen.springboot.tomcat.config

import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics
import io.github.mweirauch.micrometer.jvm.extras.ProcessThreadMetrics
import io.micrometer.core.instrument.binder.MeterBinder
import io.micrometer.core.instrument.binder.tomcat.TomcatMetrics
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ObservabilityConfig {
    @Bean
    fun processMemoryMetrics(): MeterBinder = ProcessMemoryMetrics()

    @Bean
    fun processThreadMetrics(): MeterBinder = ProcessThreadMetrics()

    @Bean
    fun tomcatMetrics(): MeterBinder = TomcatMetrics(null, emptyList())
}
