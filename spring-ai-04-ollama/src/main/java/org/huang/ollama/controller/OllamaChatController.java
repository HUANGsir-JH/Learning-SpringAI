package org.huang.ollama.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OllamaChatController {
    
    @Resource
    private OllamaChatModel chatModel;
    
    @GetMapping("/ollama/chat")
    public Object chat(@RequestParam(value = "msg", defaultValue = "Hello") String message) {
        return chatModel.call(new Prompt(message)).getResult().getOutput().getText();
    }
}
