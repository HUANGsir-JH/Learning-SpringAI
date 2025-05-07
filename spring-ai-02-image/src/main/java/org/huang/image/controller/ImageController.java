package org.huang.image.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.zhipuai.ZhiPuAiImageModel;
import org.springframework.ai.zhipuai.ZhiPuAiImageOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {
    
    @Resource
    private ZhiPuAiImageModel imageModel;
    
    @GetMapping("/image")
    public Object generateImage(@RequestParam(value = "msg") String msg){
        ImageResponse imageResponse = imageModel.call(new ImagePrompt(msg));
        System.out.println(imageResponse.getResult().getOutput().getUrl());
        return imageResponse.getResult().getOutput().getUrl();
    }
    
    @GetMapping("/image2")
    public Object generateImage2(@RequestParam(value = "msg") String msg){
        ImageResponse imageResponse = imageModel.call(new ImagePrompt(msg,ZhiPuAiImageOptions.builder()
                .model("cogview-4-250304")
                .build()
                // 传入模型参数，具体可以设定的选项取决于实现类，目前（2025.4.29）仅智谱和openai
                // 查看源码，智谱仅可设置model和user
                // openai可以设置width、height、quality等
        ));
        return imageResponse;
    }
}
