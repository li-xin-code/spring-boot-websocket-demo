package com.lixin.springbootwebsocketdemo.ws;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lixin
 */
@Slf4j
@Component
public class MyInterceptor implements HandshakeInterceptor {

    /**
     * 握手前
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        HttpHeaders headers = request.getHeaders();
        headers.forEach((k, v) -> {
            log.info("{}: {}", k, v);
        });
        log.info("handshake begin.");
        String hostName = request.getRemoteAddress().getHostName();
        String sessionId = hostName + "#" + ThreadLocalRandom.current().nextInt(1000);
        if (Strings.isNotBlank(sessionId)) {
            // 放入属性域
            attributes.put("session_id", sessionId);
            log.info("session_id [" + sessionId + "] handshake success.");
            return true;
        }
        log.info("handshake fail.");
        return false;
    }

    /**
     * 握手后
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        log.info("handshake end.");
    }

}

