package dev.danvega.blog_agent;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "blog-agent")
public record BlogAgentProperties(String outputDir) {

    public BlogAgentProperties {
        if (outputDir == null || outputDir.isBlank()) {
            outputDir = "blog-posts";
        }
    }
}
