package dev.danvega.blog_agent;

import java.util.List;

public record FrontMatter(String description, List<String> tags, List<String> keywords, String readTime) {
}
