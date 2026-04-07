package dev.danvega.blog_agent;

import com.embabel.agent.api.annotation.LlmTool;
import org.springframework.stereotype.Component;

@Component
public class ReadingStatsTool {

    private static final int WORDS_PER_MINUTE = 200;

    @LlmTool(description = "Calculate word count and estimated reading time (in minutes) for a piece of text. Reading speed is assumed to be 200 words per minute.")
    public String calculateReadingStats(
            @LlmTool.Param(description = "The full text of the blog post to analyze") String text
    ) {
        if (text == null || text.isBlank()) {
            return "0 words, 0 min read";
        }
        int words = text.trim().split("\\s+").length;
        int minutes = Math.max(1, (int) Math.ceil(words / (double) WORDS_PER_MINUTE));
        return String.format("%d words, %d min read", words, minutes);
    }
}
