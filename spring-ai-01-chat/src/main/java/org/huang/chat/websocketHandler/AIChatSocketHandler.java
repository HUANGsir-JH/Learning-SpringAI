package org.huang.chat.websocketHandler;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AIChatSocketHandler extends TextWebSocketHandler {
    
    @Resource
    private OpenAiChatModel chatModel;
    
    private final Map<String, List<Message>> sessionHistory = new ConcurrentHashMap<>();
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();
        // 获取当前会话的历史记录
        String sessionId = session.getId();
        
        // 获取或者创建该会话的历史记录
        List<Message> history = sessionHistory.computeIfAbsent(sessionId, k -> new ArrayList<>());
        
        // 添加用户信息
        history.add(new UserMessage(msg));
        
        StringBuilder responseBuilder = new StringBuilder();
        
        // 发送包含历史消息的提示到模型
        chatModel.stream(new Prompt(history,
                        ChatOptions.builder().build()))
                .subscribe(
                        chatResponse -> {
                            try {
                                String text = chatResponse.getResult().getOutput().getText();
                                session.sendMessage(new TextMessage(text));
                                responseBuilder.append(text);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            try {
                                session.sendMessage(new TextMessage("错误：" + error.getMessage()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        },
                        () -> {
                            try {
                                // 将AI的完整回复添加到历史记录
                                history.add(new AssistantMessage(responseBuilder.toString()));
                                session.sendMessage(new TextMessage("[DONE]"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                );
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 连接关闭时，清除该会话的历史记录
        sessionHistory.remove(session.getId());
    }
    
}
