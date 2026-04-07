package dev.danvega.blog_agent;

public record FinalPost(String title, String content, String feedback) implements BlogPost {
}
