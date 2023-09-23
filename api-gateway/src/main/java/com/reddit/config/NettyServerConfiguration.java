package com.reddit.config;

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class NettyServerConfiguration implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

    private static final int MAX_HEADER_SIZE = 1073741824; //1 GB

    public void customize(NettyReactiveWebServerFactory factory) {
        factory.addServerCustomizers(server ->
                server.httpRequestDecoder(decoder -> decoder.maxHeaderSize(MAX_HEADER_SIZE)));
    }
}
