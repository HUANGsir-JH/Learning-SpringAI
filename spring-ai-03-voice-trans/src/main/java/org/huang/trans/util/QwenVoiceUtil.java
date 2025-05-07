package org.huang.trans.util;

import com.alibaba.dashscope.audio.asr.recognition.Recognition;
import com.alibaba.dashscope.audio.asr.recognition.RecognitionParam;
import com.alibaba.dashscope.audio.asr.translation.TranslationRecognizerParam;
import com.alibaba.dashscope.audio.asr.translation.TranslationRecognizerRealtime;
import com.alibaba.dashscope.audio.asr.translation.results.TranscriptionResult;
import com.alibaba.dashscope.audio.asr.translation.results.TranslationRecognizerResultPack;
import com.alibaba.dashscope.audio.asr.translation.results.TranslationResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

@Component
@ConfigurationProperties(prefix = "qwen")
public class QwenVoiceUtil {
    @Value("${qwen.api-key}")
    private String apiKey;
    
    private String audioFilePath = "D:\\IDEA_project\\SpringAI\\spring-ai-03-voice-trans\\src\\main" +
            "\\resources\\voice\\hello_world.wav";
    
    private File resultFile = new File("D:\\IDEA_project\\SpringAI\\spring-ai-03-voice-trans\\src\\main" +
            "\\resources\\result\\result.json");
    
    /**
     * 语音识别(单文件)
     */
    public void recognizeSpeechFromSingleFile(){
        System.out.println(apiKey);
        // 创建识别参数
        RecognitionParam param = RecognitionParam.builder()
                .model("paraformer-realtime-v2")// 模型名称
                .format("wav")// 音频格式
                .sampleRate(16000)// 采样率
                .apiKey(apiKey)// API Key
                .build();
        
        // 创建识别器
        Recognition recognizer = new Recognition();
        
        // 定义音频文件
        System.out.println("filePath: " + audioFilePath);
        Path audioFile = Path.of(audioFilePath);
        
        // 进行语音识别
        String result = recognizer.call(param,audioFile.toFile());
        
        // 保存结果到文件
        try(FileOutputStream fos = new FileOutputStream(resultFile)){
            fos.write(result.getBytes());
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        
        // 打印结果
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = gson.fromJson(result,JsonObject.class);
        
        if(jsonObject.has("sentences")){
            for(JsonElement sent : jsonObject.get("sentences").getAsJsonArray()){
                JsonObject sentObj = sent.getAsJsonObject();
                String text = sentObj.get("text").getAsString();
                System.out.println(text);
            }
        }
        
        // 输出性能指标
        System.out.println(
                "[Metric] requestId: " + recognizer.getLastRequestId() +
                        ", first package delay ms: " + recognizer.getFirstPackageDelay() +
                        ", last package delay ms: " + recognizer.getLastPackageDelay());
    }
    
    /**
     * 语音翻译(同步)
     */
    public void translateSingleFile(){
        String targetLanguage = "en"; // 目标语言
        TranslationRecognizerRealtime translator = new TranslationRecognizerRealtime();
        // 创建识别参数
        TranslationRecognizerParam param =
                TranslationRecognizerParam.builder()
                        .model("gummy-realtime-v1")
                        .format("wav")
                        .apiKey(apiKey)
                        .sampleRate(16000)
                        .transcriptionEnabled(true)// 是否开启用识别功能。
                        .sourceLanguage("auto")
                        .translationEnabled(true)// 是否开启翻译功能。
                        .translationLanguages(new String[] {targetLanguage})
                        .build();
        
        // 定义音频文
        File file = new File(audioFilePath);
        // 进行语音识别
        TranslationRecognizerResultPack result = translator.call(param, file);
        
        if (result.getError() !=null){
            System.out.println("Error: " + result.getError());
            throw new RuntimeException(result.getError());
        }else{
            System.out.println("RequestId: " + result.getRequestId());
            
            // 打印转录结果
            System.out.println("Transcription Result:");
            for (TranscriptionResult transcription : result.getTranscriptionResultList()) {
                System.out.println(transcription.getText());
            }
        }
        
        // 打印翻译结果
        System.out.println("English Translation Result:");
        for (TranslationResult translation : result.getTranslationResultList()) {
            System.out.println(translation.getTranslation(targetLanguage).getText());
        }
    }
    
    
}
