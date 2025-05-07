package org.huang.chat.config;

import org.huang.chat.websocketHandler.AIChatSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    
    private final AIChatSocketHandler webSocketHandler;
    
    public WebSocketConfig(AIChatSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }
    
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws/chat")
                .setAllowedOrigins("*"); // 允许所有来源
    }
}
