# Blog Agent

An AI-powered blog post writer and reviewer built with Spring Boot and the [Embabel Agent Framework](https://repo.embabel.com). Give it a topic, and it drafts a beginner-friendly blog post, reviews it with a separate LLM, and saves the polished result as Markdown.

## Quick Start

```bash
export OPENAI_API_KEY=your-key-here
./mvnw spring-boot:run
```

The app launches an interactive shell. Enter a topic and the agent will:

1. **Draft** a blog post using `gpt-5-mini`
2. **Review and polish** the draft using `gpt-5`
3. **Save** the final Markdown file to `blog-posts/`

## Prerequisites

- Java 23+
- An [OpenAI API key](https://platform.openai.com/api-keys) with access to `gpt-5-mini` and `gpt-5`

## Configuration

Configuration lives in `src/main/resources/application.yaml`:

| Property | Default | Description |
|---|---|---|
| `OPENAI_API_KEY` | — | OpenAI API key (env variable) |
| `blog-agent.output-dir` | `blog-posts` | Directory where finished posts are saved |
| `embabel.models.default-llm` | `gpt-5-mini` | Model used for drafting |
| `embabel.models.llms.reviewer` | `gpt-5` | Model used for reviewing |

## How It Works

The `BlogWriterAgent` defines a two-step Embabel agent pipeline:

- **`writeDraft`** — Prompts the default LLM to write a practical, beginner-friendly blog post in Markdown (no code examples).
- **`reviewDraft`** — Sends the draft to a reviewer LLM for technical editing and tighter writing. The reviewed post is saved to disk as a slug-named `.md` file.

## Shell Commands

The app runs in an interactive Spring Shell. Type `help` to see all available commands. Here are the most useful ones:

| Command | Description |
|---|---|
| `x <topic>` | Execute the agent with a given topic (e.g., `x "Getting started with Spring Boot"`) |
| `x -p <topic>` | Execute and print the exact prompts sent to the LLM |
| `agents` | List all available agents |
| `actions` | List all available actions agents can perform |
| `goals` | List all available goals |
| `models` | List available language models |
| `blackboard` / `bb` | Show the last blackboard state (working memory from the previous run) |
| `clear` | Clear the blackboard |
| `runs` | Show recent agent runs with cost information |
| `chat` | Start an interactive chat session |
| `platform` | Show information about the AgentPlatform |
| `help` | Show all available commands |

The `-p` flag on `x` is especially useful for debugging — it shows you exactly what prompts are being sent to each LLM in the pipeline.

## What's Next

The current agent has two actions: draft and review. Here are some ideas for expanding the pipeline with additional actions:

**Content Creation**
- **Write a catchy title** — Generate multiple title options and pick the strongest one
- **Write a hook** — Craft an engaging opening paragraph that pulls readers in
- **Generate a TL;DR** — Short summary for social sharing or email newsletters

**SEO & Discovery**
- **SEO optimization** — Suggest meta description, slug, and keywords
- **Generate tags/categories** — Auto-classify the post into your blog's taxonomy
- **Write social media posts** — Generate Twitter/LinkedIn snippets to promote the article

**Quality & Polish**
- **Fact checker** — Verify technical claims against known sources
- **Readability scorer** — Evaluate reading level and suggest simplifications
- **Add code examples** — Optionally enrich the post with relevant code snippets

**Publishing Pipeline**
- **Generate frontmatter** — Produce YAML frontmatter (title, date, tags, description) for static site generators
- **Create outline first** — Generate a structured outline before drafting to steer direction
- **Thumbnail prompt generator** — Write an image generation prompt for a hero image

Because Embabel resolves action order from input/output types, new actions can be chained into the pipeline simply by declaring the right types (e.g., `BlogDraft → TitledDraft → SEOEnrichedPost → ReviewedPost`).

## Tech Stack

- Spring Boot 3.5
- Embabel Agent Framework 0.4.0
- Spring AI 1.1.4
- Java 23
