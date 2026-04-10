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

    @AchievesGoal(description = "A reviewed and polished blog post")
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
