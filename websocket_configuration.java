package com.example.docs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Topic prefix for outbound broadcast messages
        config.enableSimpleBroker("/topic");
        // Prefix for inbound client messages
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Handshake endpoint for WebSocket connections
        registry.addEndpoint("/ws-editor").setAllowedOriginPatterns("*").withSockJS();
    }
}