package com.example.tt.Controller;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Component
@ServerEndpoint(value = "/chat")
public class SocketTest  {

    @OnOpen
    public void onOpen(Session session ) throws IOException {
        session.getBasicRemote().sendText("sadasd");
    }

    @OnMessage
    public void OnMessage(String message, Session session) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter()
    {
        return new ServerEndpointExporter();
    }

}
