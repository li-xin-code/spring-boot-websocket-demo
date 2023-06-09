package com.lixin.springbootwebsocketdemo.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lixin
 */
@Component
@Slf4j
public class HttpAuthHandler extends TextWebSocketHandler {

    /**
     * socket 建立成功事件
     *
     * @param session ...
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Object sessionId = session.getAttributes().get("session_id");
        if (sessionId != null) {
            // 用户连接成功，放入在线用户缓存
            WsSessionManager.add(sessionId.toString(), session);
        } else {
            throw new RuntimeException("用户登录已经失效!");
        }
    }

    /**
     * 接收消息事件
     *
     * @param session ...
     * @param message ...
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        messageEcho(session, message);
    }

    private void messageEcho(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        Object sessionId = session.getAttributes().get("session_id");
        log.info("server 接收到 " + sessionId + " 发送的 " + payload);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String msg = String.format("server receive [%s] from [%s] on [%s]", payload,
                    sessionId, LocalDateTime.now().format(formatter));
            session.sendMessage(new TextMessage(msg));
        } catch (IOException e) {
            log.error("message echo fail.");
        }
    }

    /**
     * socket 断开连接时
     *
     * @param session ...
     * @param status  ...
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Object sessionId = session.getAttributes().get("session_id");
        if (sessionId != null) {
            // 用户退出，移除缓存
            WsSessionManager.remove(sessionId.toString());
        }
    }

}

