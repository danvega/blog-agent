package dev.danvega.blog_agent;

public record DraftPost(String title, String content) implements BlogPost {
}
