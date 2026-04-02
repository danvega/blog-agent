# How to Write Your First Technical Conference Talk

For first-time speakers. Practical. Short. Plain language.

## 1. Pick one clear idea
- Choose a single message your audience should remember. Write it in one sentence.
Example: 'Make Python web requests faster with async.'

## 2. Know your audience
- Ask who will attend: beginner, intermediate, or advanced.
- Use their vocabulary. Avoid jargon or define it.

## 3. Build a simple outline
- Use a three-part structure:
  1) Hook and main message.
  2) Two or three concrete points or demos.
  3) Short summary and next steps.
Example outline:
- Intro (1–2 slides)
- Problem (1 slide)
- Approach (2 slides)
- Code demo (2 slides)
- Results and tips (1 slide)
- Wrap-up and Q&A (1 slide)

## 4. One idea per slide
- Keep each slide focused on one idea.
- Use large text and a single image or small code snippet.
Good slide text:
- 'Problem: Slow HTTP requests'
- 'Solution: Use async to run requests in parallel'
Bad:
- Long paragraphs and dense text.

## 5. Short, clear code examples
- Show small, runnable examples. Explain each line briefly. Favor clarity over completeness.

JavaScript example:

```javascript
async function fetchAll(urls) {
  const responses = await Promise.all(urls.map(u => fetch(u)));
  return responses.map(r => r.status);
}
```

Python example (reuse one session):

```python
import asyncio
import aiohttp

async def fetch(session, url):
    async with session.get(url) as r:
        return await r.text()

async def fetch_all(urls):
    async with aiohttp.ClientSession() as session:
        return await asyncio.gather(*(fetch(session, u) for u in urls))
```

- Show the minimal working idea. Do not show the whole app.

## 6. Write a short script for each slide
- Write 1–3 sentences you will say per slide. Keep it natural. Use the script to guide practice.
Example for an intro slide:
'Hi, I'm Alex. Today I'll show a simple way to speed up many HTTP calls. You'll see a short demo and an easy change you can apply today.'

## 7. Time the talk
- Confirm whether Q&A is included in your slot.
- Practice with a timer. Aim to finish 2 minutes early for transitions and questions.
Rule of thumb:
- 20-minute talk: 12–15 slides
- 45-minute talk: 25–30 slides

## 8. Practice out loud
- Record yourself. Practice in front of a friend. Time the demo steps. Fix awkward phrases.
Practice checklist:
- Slide flow works.
- Code runs on your laptop.
- Demo is repeatable in under 3 minutes.

## 9. Handle demos safely
- Have a backup plan: screenshots or a short screen recording.
- Make the demo work offline. Pin versions and seed data.

## 10. Slide design tips
- Large fonts (at least 28–32 pt for body text).
- One image or diagram per slide.
- High-contrast, colorblind-safe colors.
- Avoid dense charts; highlight the key point.

## 11. Use signposts and transitions
- Tell the audience where you are: 'Now I'll show the demo.' 'Quick recap.'

## 12. Prepare for Q&A
- Anticipate three common questions. Prepare short answers.
- If you do not know, say so and offer to follow up.

## 13. Final checklist before the conference
- Laptop charged and updated; notifications off.
- Slides exported to PDF as backup; copy on a USB drive or shared link.
- Adapters, clicker, and cables packed.
- Code and data available locally; environment pinned.
- Notes printed or on a tablet.
- One-sentence takeaway ready.
- Arrive early for mic and projector check.

## Sample 7-minute slide plan
- Slide 1: Title + one-sentence takeaway (30s)
- Slide 2: Problem (45s)
- Slide 3: Approach (45s)
- Slide 4: Quick code example (90s)
- Slide 5: Demo or screenshots (90s)
- Slide 6: Results and tips (60s)
- Slide 7: Wrap-up + call to action (30s)

## Closing advice
Keep it simple. Teach one thing well. Practice until it feels natural. Short, clear talks stick.

Good luck. Start small. Improve with each talk.