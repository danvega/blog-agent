package dev.danvega.blog_agent;

public record PublishedPost(String title, String content, String feedback) implements BlogPost {
}
