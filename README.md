# Jadicaped

A RuneLite plugin for the TzTok-Jad phase of the Fight Caves.

This project is intentionally static and informational: the plugin shows a small in-client focus overlay and written reminders for the Jad prayer cycle, without automating inputs or making decisions for the player.

## Post-Death Sidebar Summary module

This module adds a retrospective-only sidebar panel for after a Jad attempt ends. It is intentionally designed to update only after death or attempt completion, never during the active fight. The panel keeps the summary static and readable, with a plain-text export button.

### Included summary fields

- Attempt Summary
  - Time survived
  - Wave reached
  - Final hit type
  - Final hit damage
  - Prayer state at death
  - HP at death
- Prayer Performance
  - Prayer accuracy %
  - Missed prayers
  - Ticks unprotected
  - Longest unprotected streak
  - Total prayer switches
- Damage Breakdown
  - Total damage taken
  - Damage while protected
  - Damage while unprotected
  - Damage avoided due to correct prayer
- Resource Usage
  - Brews used
  - Restores used
  - Total prayer drain
  - Overhead uptime %
- Movement Summary
  - Tiles moved
  - Movement frequency
  - Times stood still too long
  - Unnecessary movement
- Mistake Breakdown
  - Prayer mistakes
  - Movement mistakes
  - Inventory mistakes
- Confidence Score
  - Simple composite score built from accuracy, movement stability, and resource usage

## Compliance boundaries

This plugin stays within Jagex-safe boundaries:

- No real-time boss assistance
- No NPC animation detection
- No attack prediction
- No reactive overlays during combat
- No automation
- All data is passive and retrospective after death or attempt end

## Build

```bash
gradle build
```

## Community strategy for RuneLite forums and YouTube

### 1. Position it as a reflection tool, not a combat assist

This is best pitched as a calm retrospective review panel for Jad practice. It helps players reflect on prayer timing, movement, resources, and mistakes after the attempt is over, while staying completely non-reactive during the fight.

### 2. Show the compliance story clearly in the first post

When posting to RuneLite forums or making a video, lead with:

- static reminder overlay only
- no live combat prediction
- no automation
- retrospective summary after death
- designed for focus, review, and learning

This makes the intent clear and reduces the risk of confusion with live assist tools.

### 3. Content angle for YouTube

Video ideas:

- "Jad focus mode, then review my attempt with the post-death summary"
- "How I use a static Jad reminder plugin with a retrospective review panel"
- "A calm, non-reactive Jad learning setup"
- "What the post-death summary tells me after I die"
- "Reviewing prayer mistakes after a Jad clear attempt"

### 4. Forum post structure

Suggested layout:

1. short summary of the plugin goal
2. compliance note: no automation, no prediction, no live assistance
3. screenshot of the post-death summary after a death
4. explanation of the retrospective workflow
5. invitation for feedback on which fields are most useful

### 5. Improvement ideas

- add optional per-attempt save snapshots
- add a lightweight CSV export alongside plain text
- add selectable field groups for a compact mode
- add a more readable "good / risky / bad" summary for each category
- add a "compare last three attempts" panel for progression tracking
- add a toggle for a cleaner, more minimal post-death layout

### 6. Content and trust checklist

- keep screenshots free of deceptive overlays
- explain that no live help is included during combat
- show the plugin as a player aid and review tool
- avoid wording like “bot”, “aim assist”, or “auto-prayer"
- keep the documentation honest and initial-version labeled until the feature set is tested and polished in-game

### 7. Best tone to use online

The safest positioning is:

- calm and educational
- retrospective and self-review focused
- honest about the plugin being static and non-reactive
- clear that it is not a live combat decision engine

This tone is much more likely to resonate well with RuneLite users and avoid misinterpretation.

## Notes

- Intended for Old School RuneScape
- Built for RuneLite plugin conventions
- Designed to be read-only and non-automated
- Intended to help players review performance after the attempt ends, not during it
