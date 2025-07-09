package com.myfitmate.myfitmate.domain.ai;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class OpenAiConfig {
    @Value("${openai.api-key}")
    private String apiKey;
}