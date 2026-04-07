# Blog Agent

An AI-powered blog post writer and reviewer built with Spring Boot and the [Embabel Agent Framework](https://repo.embabel.com). Give it a topic, and it researches, drafts, reviews, summarizes, and publishes a beginner-friendly blog post as Markdown — using both MCP-based and custom Java tools along the way.

## Quick Start

```bash
export ANTHROPIC_API_KEY=your-anthropic-key
export OPENAI_API_KEY=your-openai-key
export BRAVE_API_KEY=your-brave-key
./mvnw spring-boot:run
```

The app launches an interactive shell. Type `x "your topic"` and the agent will run a 5-stage pipeline producing a finished Markdown file in `blog-posts/`.

## Prerequisites

- Java 23+
- [Anthropic API key](https://console.anthropic.com/) — used by the default and reviewer LLMs (`claude-sonnet-4-6`, `claude-opus-4-6`)
- [OpenAI API key](https://platform.openai.com/api-keys) — referenced by the platform config
- [Brave Search API key](https://brave.com/search/api/) — used by the web research tool (free tier works)
- Node.js / `npx` — required so the Brave Search MCP server can be launched

## Configuration

Configuration lives in `src/main/resources/application.yaml`:

| Property | Default | Description |
|---|---|---|
| `ANTHROPIC_API_KEY` | — | Anthropic API key (env variable) |
| `OPENAI_API_KEY` | — | OpenAI API key (env variable) |
| `BRAVE_API_KEY` | — | Brave Search API key (env variable) |
| `blog-agent.output-dir` | `blog-posts` | Directory where finished posts are saved |
| `blog-agent.number-of-keywords` | `5` | Max keywords generated for front matter |
| `embabel.models.default-llm` | `claude-sonnet-4-6` | Model used for drafting, research, TLDR, and front matter |
| `embabel.models.llms.reviewer` | `claude-opus-4-6` | Model used for reviewing |

## How It Works

The `BlogWriterAgent` defines a five-stage Embabel pipeline. Each stage is an `@Action` method whose input/output types let Embabel chain them automatically.

1. **`researchTopic`** — Uses the LLM with `CoreToolGroups.WEB` (a tool group backed by the Brave Search MCP server) to research the topic on the web before any writing happens.
2. **`writeDraft`** — Drafts a practical, beginner-friendly Markdown post using the research findings.
3. **`reviewDraft`** — Sends the draft to a stronger reviewer LLM for technical editing and tighter writing.
4. **`addTldr`** — Generates a one-or-two sentence TLDR and prepends it to the post.
5. **`addFrontMatter`** — Uses the **`ReadingStatsTool`** (a custom Java `@LlmTool` component) to compute exact word count and read time, then generates YAML front matter (description, tags, keywords, readTime) and writes the finished file to disk.

### Tool Use: Two Flavors

This project demonstrates both ways to give an LLM tools in Embabel:

- **MCP tools** — `researchTopic` uses `.withToolGroup(CoreToolGroups.WEB)`. Embabel resolves this against the Brave Search MCP server configured under `spring.ai.mcp.client.stdio.connections` in `application.yaml`. The MCP server is launched on demand via `npx`.
- **Custom Java tools** — `ReadingStatsTool` is a plain Spring `@Component` with one method annotated `@LlmTool`. The `addFrontMatter` action wires it in with `.withToolObject(readingStatsTool)`. No MCP, no external service — just Java.

Both approaches surface to the LLM as standard tool calls; the LLM decides when to invoke them.

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

Ideas for expanding the pipeline with additional actions:

**Content Creation**
- **Write a catchy title** — Generate multiple title options and pick the strongest one
- **Write a hook** — Craft an engaging opening paragraph that pulls readers in

**SEO & Discovery**
- **Write social media posts** — Generate Twitter/LinkedIn snippets to promote the article

**Quality & Polish**
- **Fact checker** — Verify technical claims against known sources (great fit for another tool!)
- **Readability scorer** — Evaluate reading level and suggest simplifications

**Publishing Pipeline**
- **Create outline first** — Generate a structured outline before drafting to steer direction
- **Thumbnail prompt generator** — Write an image generation prompt for a hero image

Because Embabel resolves action order from input/output types, new actions can be chained into the pipeline simply by declaring the right types (e.g., `DraftPost → TitledDraft → SEOEnrichedPost → ReviewedPost`).

## Tech Stack

- Spring Boot 3.5
- Embabel Agent Framework 0.4.0
- Spring AI 1.1.4
- Java 23
