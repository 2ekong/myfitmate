package com.myfitmate.myfitmate.domain.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class OpenAiController {

    private final OpenAiService openAiService;

    @GetMapping("/ask")
    public String ask(@RequestParam String q) {
        return openAiService.ask(q);
    }
}
