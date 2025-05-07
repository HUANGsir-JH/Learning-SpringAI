package org.huang.trans;

import org.huang.trans.util.QwenVoiceUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringAi03VoiceTransApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringAi03VoiceTransApplication.class, args);
		QwenVoiceUtil recognizeSpeechUtil = context.getBean(QwenVoiceUtil.class);
//		recognizeSpeechUtil.recognizeSpeechFromSingleFile();
		recognizeSpeechUtil.translateSingleFile();
	}

}
