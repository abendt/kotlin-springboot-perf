package redgreen.springboot.webflux.config

import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics
import io.github.mweirauch.micrometer.jvm.extras.ProcessThreadMetrics
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.core.instrument.binder.MeterBinder
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.netty.http.server.HttpServer

@Configuration
class ObservabilityConfig {

    val logger = KotlinLogging.logger {}

    @Bean
    fun processMemoryMetrics(): MeterBinder = ProcessMemoryMetrics()

    @Bean
    fun processThreadMetrics(): MeterBinder = ProcessThreadMetrics()

    @Bean
    fun nettyServerCustomizer(): NettyServerCustomizer =
        NettyServerCustomizer { httpServer: HttpServer -> httpServer.metrics(true, ::identity) }

    @Bean
    @ConditionalOnProperty("tracing.url")
    fun otlpHttpSpanExporter(@Value("\${tracing.url}") url: String): OtlpGrpcSpanExporter {
        logger.info { "Enabling OtlpGrpc export to $url" }

        return OtlpGrpcSpanExporter.builder()
            .setEndpoint(url).build()
    }

    private fun identity(str: String): String = str
}
