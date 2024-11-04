package redgreen.springboot.webflux.config

import io.netty.channel.ChannelOption
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.stereotype.Component

@Component
class NettyConfig : WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {
    override fun customize(factory: NettyReactiveWebServerFactory) {
        factory.serverCustomizers.add {
            it.option(ChannelOption.SO_BACKLOG, 2048)
        }
    }
}
