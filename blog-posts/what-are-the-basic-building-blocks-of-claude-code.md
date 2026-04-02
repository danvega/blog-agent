# What are the basic building blocks of Claude Code

Claude Code helps you write and refine real code.
This post covers the core parts you’ll use when building with Claude—kept short, practical, and example-driven.

## 1. Prompts (system and user)

Prompts tell Claude what to do. Be explicit. Prefer short, direct instructions. Split durable guidance (system) from the specific task (user).

Example prompt template:
```
System: You are a helpful coding assistant.
User: Write a Python function to reverse words in a sentence. Include tests.
```

Clear prompts lead to clearer code.

## 2. Instructions and constraints

State constraints and expectations up front: required inputs, output format, limits, and non-goals.

Example:
```
User: Write a function reverse_words(text: str) -> str.
Return only the function code. Do not include explanation.
Limit line length to 80 characters.
```

## 3. Code blocks

Claude returns code in fenced blocks. Ask for language tags (e.g., ```python) for syntax highlighting. Copy those blocks into your editor or run them.

Example output you might get:
```python
def reverse_words(text: str) -> str:
    return " ".join(text.split()[::-1])
```

## 4. Files and project structure

Treat model output as files. Specify exact file names and paths. Ask for multiple files when needed.

Example request:
```
User: Create two files:
- src/utils.py with a reverse_words function.
- tests/test_utils.py with pytest tests for it.
```

Example file content returned:
```python
# src/utils.py
def reverse_words(text: str) -> str:
    return " ".join(text.split()[::-1])
```

## 5. Tests and examples

Ask for unit tests. Tests give you confidence and a fast feedback loop. Run tests locally and feed failures back to Claude to fix code.

Example pytest test:
```python
# tests/test_utils.py
from src.utils import reverse_words

def test_reverse_words():
    assert reverse_words("hello world") == "world hello"
```

## 6. Run / fix loop

The loop is: ask → run → see error → ask again. Share exact error messages and stack traces. Be precise and include file names and line numbers.

Example interaction:
```
User: I ran tests and got:
IndexError: list index out of range in utils.py line 3
Please fix the function and add a new test for empty input.
```

Tight, specific feedback leads to targeted fixes.

## 7. Function and tool calls (when available)

If your setup supports tool use (running code, fetching files, calling APIs), use it to get real outputs. Paste exact tool outputs back to Claude.

Example (pseudo):
```
Tool output: pytest failed with 1 failed, 0 passed
User: Here is the pytest output. Fix the failing test.
```

## 8. Docstrings and comments

Ask for docstrings and short comments to aid future maintenance.

Example:
```python
def reverse_words(text: str) -> str:
    """Return the words in text in reverse order."""
    return " ".join(text.split()[::-1])
```

## 9. Small, focused tasks

Keep each request small—one task per prompt works best.

Bad: Build the whole app.
Good: Create a CLI to do X with two commands.

## 10. Iteration and version control

Commit small changes with meaningful messages. Ask Claude to propose commit messages and changelog entries.

Example commit message:
```
Add reverse_words function and pytest unit test.
```

## Quick practical example: full flow

1. Prompt: ask for function and tests.
2. Copy code to files.
3. Run tests.
4. Send back errors to Claude.
5. Repeat until green.

Example short prompts and files (summary):
```
User: Create src/utils.py and tests/test_utils.py. Function: reverse_words.
```

src/utils.py:
```python
def reverse_words(text: str) -> str:
    """Return words in reverse order."""
    return " ".join(text.split()[::-1])
```

tests/test_utils.py:
```python
from src.utils import reverse_words

def test_reverse_words():
    assert reverse_words("a b c") == "c b a"
    assert reverse_words("") == ""
```

Run:
```
pytest
```

If a test fails, paste the failure output into a new prompt and ask Claude to fix it.

## Tips

- Be explicit. Short sentences help.
- Ask for tests. Always.
- Share exact error messages when you need fixes.
- Request only the file contents when you want copy‑paste‑ready output.
- Iterate. Small changes are easier to verify.
- Ask for fenced code blocks with language tags for easy copy/paste.

## Conclusion

Claude Code workflows are simple: use clear prompts, code blocks, tests, and a tight run/fix loop. Keep tasks small and iterate until the code works. This makes the model genuinely useful in day‑to‑day coding.