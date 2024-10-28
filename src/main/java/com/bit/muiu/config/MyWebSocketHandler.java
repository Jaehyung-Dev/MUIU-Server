package com.bit.muiu.config;

import org.json.JSONObject;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.stereotype.Component;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connected: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 수신된 payload가 JSON 형식인지 확인 후 처리
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("message", "Message received");
        jsonResponse.put("data", new JSONObject(message.getPayload()));

        // JSON 객체를 문자열로 변환하여 전송
        session.sendMessage(new TextMessage(jsonResponse.toString()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Disconnected: " + session.getId());
    }
}
