# How Coding Agents Work

Coding agents follow a simple loop: sense, decide, act. They receive observations, choose an action via a policy (rules or a model), perform the action through an environment interface, and repeat.

What an agent does

- Observe: read input from the environment (e.g., text, sensor data, state).
- Decide: map observation (and optional memory) to an action using a policy.
- Act: execute the action to change the environment (e.g., API call, file write).

Core components

- Input (observations): examples include `"hello"`, `sensor_reading`, `state`.
- Policy (decision function or model): example `def policy(obs): return action`.
- Output (actions): examples include `send_command(action)`, `write_file(...)`.

Simple loop

1. Get an observation.
2. Compute an action with the policy.
3. Apply the action.
4. Repeat.

Minimal control loop (pseudocode)

`while True:`
`    obs = get_observation()`
`    action = policy(obs)`
`    apply_action(action)`

A very small Python agent

`def agent(obs):`
`    if obs == "hello":`
`        return "hi"`
`    return "ok"`

`print(agent("hello"))  # hi`

A tiny JavaScript example

`async function agent(msg) {`
`  if (msg === "ping") return "pong";`
`  return "unknown";`
`}`

`agent("ping").then(console.log);  // pong`

Notes for beginners

- Keep functions small and single-purpose.
- Define clear inputs and outputs; avoid hidden side effects.
- Test with simple, representative observations.
- Log inputs, decisions, and outcomes for debugging and traceability.

Adding memory (state)

- Keep a short history to inform decisions.

`history = []`
`def agent(obs):`
`    history.append(obs)`
`    if obs == "hello":`
`        return "hi"`
`    return "ok"`

Policy types

- Rule-based: if-else rules. Easiest to start; predictable.
- ML-based: learned models (e.g., classifiers, LLMs). More work; more flexible.
- Hybrid: combine rules with a model for reliability and coverage.

Safety and limits

- Validate and sanitize inputs.
- Add timeouts and retries with backoff for external calls.
- Rate-limit actions; use circuit breakers for failing dependencies.
- Fail gracefully and surface clear errors.
- Prefer dry-run modes and sandboxed environments in development.

Next steps

- Wrap the policy in a small loop that runs until a stop condition.
- Experiment with different policies (rules first, then a simple model).
- Add tests that assert outputs for given inputs and include edge cases.
- Capture logs and metrics to inspect behavior over time.
