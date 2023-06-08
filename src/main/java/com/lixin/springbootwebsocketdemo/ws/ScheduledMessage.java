package com.lixin.springbootwebsocketdemo.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lixin
 * @date 2023/6/8 22:06
 */
@Component
@Slf4j
public class ScheduledMessage implements InitializingBean {
    private final ScheduledExecutorService scheduledExecutorService =
            new ScheduledThreadPoolExecutor(3, new SimpleThreadFactory());

    @Override
    public void afterPropertiesSet() {
        scheduledExecutorService.scheduleAtFixedRate(() ->
                WsSessionManager.getAll().forEach(webSocketSession -> {
                    TextMessage message = new TextMessage("heartbeat message");
                    try {
                        webSocketSession.sendMessage(message);
                    } catch (IOException e) {
                        log.error("send message fail.", e);
                    }
                }), 5, 10, TimeUnit.SECONDS);
    }


    private static class SimpleThreadFactory implements ThreadFactory {
        private final AtomicInteger atomicInteger = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ScheduledMessage-" + atomicInteger.getAndIncrement());
        }
    }
}
