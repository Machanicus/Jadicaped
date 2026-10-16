# Jadicaped

Jadicaped is a RuneLite focus dimmer for Old School RuneScape. While enabled, it gently dims the client canvas while leaving the current player, TzTok-Jad, minimap, health/prayer controls, and game tab controls undimmed. Inventory and chat remain visible beneath the tint. The tint is suspended while a right-click menu is open. It can enable automatically inside the Fight Caves, with a configurable hotkey override.

The mask follows only the current player and Jad model silhouettes. It does not read attack animations or projectiles, recommend prayers, predict attacks, or automate input. Because the plugin is Jad-specific, Plugin Hub acceptance is not guaranteed; RuneLite has previously removed a Fight Cave helper.

## Post-Death Sidebar Summary Prototype

The sidebar is optional in the plugin's settings and currently displays/exports a supplied summary object. It does not collect attempt data or populate the summary automatically; the fields below are planned, not implemented metrics.

### Planned summary fields

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

The live focus mask is a visual dimming effect only; it does not interpret combat state. The actor cutouts update with the current model positions, so maintainers must review the Jad-specific behavior before any Plugin Hub submission.

## Build

```bash
gradle build
```

## Current limitations

- The post-death sidebar is a display/export prototype; no code collects or supplies attempt data.
- Automatic activation uses the instanced world location and has not been verified in a live Fight Caves run.
- The focus mask has not been visually verified in a running RuneLite client or across fixed/resizable layouts.
- Jad-specific actor cutouts require Plugin Hub review; this project does not claim approval or guaranteed compliance.
