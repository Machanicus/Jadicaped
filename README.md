# Jadicaped

Jadicaped is a RuneLite focus dimmer for Old School RuneScape. While enabled, it gently dims the client canvas while leaving the current player, TzTok-Jad, minimap, health/prayer controls, and game tab controls undimmed. Inventory and chat remain visible beneath the tint. The tint is suspended while a right-click menu is open. It can enable automatically inside the Fight Caves, with a configurable hotkey override.

The mask follows only the current player and Jad model silhouettes. It does not read attack animations or projectiles, recommend prayers, predict attacks, or automate input. Because the plugin is Jad-specific, Plugin Hub acceptance is not guaranteed; RuneLite has previously removed a Fight Cave helper.

## Pre-Jad Logout Reminder

When the game reports wave 62, the plugin briefly flashes an optional-break reminder before TzTok-Jad on wave 63. The reminder clears when the flash ends, wave 63 starts, or you leave the caves. The Fight Cave wiki explains that requesting logout during a wave lets it finish, then logs you out with the next wave paused until you log back in; fully logging out before the wave finishes restarts that wave. See the [Fight Cave wiki](https://oldschool.runescape.wiki/w/TzHaar_Fight_Cave) sections on Jad and logout tips for details.

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

The live focus mask identifies the local player and Jad to create transparent cutouts. It does not analyze attack animations, projectiles, or prayer state, recommend combat actions, indicate safe tiles, or automate input. The wave-62 notice uses the game's existing wave message and only explains the built-in logout pause. RuneLite previously removed a Fight Cave plugin that gave prayer guidance, so this separate Jad-specific visual feature still needs Plugin Hub review; acceptance is not guaranteed.

## Build

```bash
gradle build
```

## Current limitations

- The post-death sidebar is a display/export prototype; no code collects or supplies attempt data.
- Automatic activation uses the instanced world location and has not been verified in a live Fight Caves run.
- The focus mask has not been visually verified in a running RuneLite client or across fixed/resizable layouts.
- Jad-specific actor cutouts require Plugin Hub review; this project does not claim approval or guaranteed compliance.
