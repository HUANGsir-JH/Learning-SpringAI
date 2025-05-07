package org.huang.chat.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    @Resource
    private OpenAiChatModel chatModel; // 注入OpenAiChatModel，springAI自动配置
    
    @GetMapping("/chat")
    public String chat(@RequestParam(value="msg") String msg){
        return chatModel.call(msg);
    }
    
    @GetMapping("/chat2")
    public Object chat2(@RequestParam(value="msg") String msg){
        ChatResponse response = chatModel.call(new Prompt(msg));
        return response.getResult().getOutput().getText();
    }
    
    @GetMapping("/chat3")
    public Object chat3(@RequestParam(value="msg") String msg) {
        // 配置文件和代码都配置了，代码优先
        ChatResponse response = chatModel.call(new Prompt(msg, ChatOptions.builder()
                .model("deepseek-chat")
                .temperature(0.8)
                .build()));
        return response.getResult().getOutput().getText();
    }
    
    // 流式返回
    @GetMapping("/chat4")
    public Object chat4(@RequestParam(value = "msg") String msg){
        Flux<ChatResponse> stream = chatModel.stream(new Prompt(msg,ChatOptions.builder().build()));
        return stream.collectList();
    }
    
    // 流式返回页面
    @GetMapping(value = "/chat5", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public  Flux<String> chat5(@RequestParam(value = "msg") String msg){
        Flux<ChatResponse> stream = chatModel.stream(new Prompt(msg,ChatOptions.builder().build()));
        return stream.map(chatResponse -> chatResponse.getResult().getOutput().getText());
    }
    
    // 代码设置模型
    @GetMapping("/chat6")
    public Object chat6(@RequestParam(value = "msg") String msg){
        ChatResponse response = chatModel.call(new Prompt(msg, ChatOptions.builder()
                .model("deepseek-reasoner")
                .temperature(0.8)
                .build()));
        return response;
    }
}
