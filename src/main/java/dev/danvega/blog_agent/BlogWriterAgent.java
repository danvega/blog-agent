package dev.danvega.blog_agent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.Ai;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.domain.io.UserInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.embabel.common.ai.model.LlmOptions;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Agent(description = "Write and review a blog post about a given topic")
public class BlogWriterAgent {

    private static final Logger log = LoggerFactory.getLogger(BlogWriterAgent.class);

    private final BlogAgentProperties properties;

    public BlogWriterAgent(BlogAgentProperties properties) {
        this.properties = properties;
    }

    @Action(description = "Write a first draft of the blog post")
    public DraftPost writeDraft(UserInput userInput, Ai ai) {
        return ai
                .withLlm(LlmOptions.withDefaults().withMaxTokens(16384))
                .withId("blog-post-draft-writer")
                .withPromptContributors(List.of(Personas.WRITER, Personas.JSON_OUTPUT))
                .creating(DraftPost.class)
                .fromPrompt("""
                        Write a blog post about: %s

                        Keep it practical and beginner friendly.
                        Use short sentences and plain language.
                        Include code examples but keep them short and simple.
                        Write the content in Markdown.
                        """.formatted(userInput.getContent())
                );
    }

    @Action(description = "Review and improve the draft")
    public ReviewedPost reviewDraft(DraftPost draft, Ai ai) {
        return ai
                .withLlm(LlmOptions.withLlmForRole("reviewer").withMaxTokens(16384))
                .withId("blog-post-reviewer")
                .withPromptContributors(List.of(Personas.REVIEWER, Personas.JSON_OUTPUT))
                .creating(ReviewedPost.class)
                .fromPrompt("""
                        Title: %s
                        Content:
                        %s

                        Fix any technical errors. Tighten the writing.
                        Provide the revised title, revised content, and a brief
                        summary of the changes you made as feedback.
                        """.formatted(draft.title(), draft.content())
                );
    }

    @Action(description = "Add a TLDR summary to the top of the blog post")
    public FinalPost addTldr(ReviewedPost post, Ai ai) {
        String tldr = ai
                .withDefaultLlm()
                .withId("blog-post-tldr")
                .creating(String.class)
                .fromPrompt("""
                        Write a one or two sentence TLDR summary for this blog post.
                        Return only the summary text, nothing else.

                        Title: %s
                        Content:
                        %s
                        """.formatted(post.title(), post.content())
                );

        String contentWithTldr = "> **TLDR:** " + tldr + "\n\n" + post.content();
        return new FinalPost(post.title(), contentWithTldr, post.feedback());
    }

    @AchievesGoal(description = "A reviewed and polished blog post with front matter")
    @Action(description = "Add front matter to the top of the blog post")
    public PublishedPost addFrontMatter(FinalPost post, Ai ai) {
        FrontMatter frontMatter = ai
                .withDefaultLlm()
                .withId("blog-post-front-matter")
                .withPromptContributors(List.of(Personas.JSON_OUTPUT))
                .creating(FrontMatter.class)
                .fromPrompt("""
                        Generate front matter metadata for this blog post.
                        Provide a concise description (1-2 sentences), relevant tags, and up to %d keywords.

                        Title: %s
                        Content:
                        %s
                        """.formatted(properties.numberOfKeywords(), post.title(), post.content())
                );

        String slug = post.title()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        String tags = frontMatter.tags().stream()
                .map(tag -> "  - " + tag)
                .collect(Collectors.joining("\n"));

        String keywords = frontMatter.keywords().stream()
                .map(keyword -> "  - " + keyword)
                .collect(Collectors.joining("\n"));

        String frontMatterBlock = """
                ---
                title: "%s"
                slug: %s
                date: "%sT08:00:00.000Z"
                published: true
                description: "%s"
                author: "Dan Vega"
                tags:
                %s
                keywords:
                %s
                ---
                """.formatted(
                post.title(),
                slug,
                LocalDate.now(),
                frontMatter.description(),
                tags,
                keywords
        );

        String contentWithFrontMatter = frontMatterBlock + "\n" + post.content();
        PublishedPost publishedPost = new PublishedPost(post.title(), contentWithFrontMatter, post.feedback());
        writeToFile(publishedPost);
        return publishedPost;
    }

    private void writeToFile(BlogPost post) {
        String filename = post.title()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "")
                + ".md";

        Path outputDir = Path.of(properties.outputDir());
        Path filePath = outputDir.resolve(filename);

        try {
            Files.createDirectories(outputDir);
            Files.writeString(filePath, post.content());
            log.info("Blog post written to {}", filePath.toAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to write blog post to {}: {}", filePath, e.getMessage());
        }
    }
}
