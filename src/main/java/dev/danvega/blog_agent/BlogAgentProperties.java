package dev.danvega.blog_agent;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "blog-agent")
public record BlogAgentProperties(String outputDir, int numberOfKeywords) {

    public BlogAgentProperties {
        if (outputDir == null || outputDir.isBlank()) {
            outputDir = "blog-posts";
        }
        if (numberOfKeywords <= 0) {
            numberOfKeywords = 5;
        }
    }
}
