package com.jadicaped;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;

@ConfigGroup("jadicaped")
public interface JadicapedConfig extends Config {
    int DEFAULT_DIM_INTENSITY = 70;
    int DEFAULT_ORB_SCALE = 150;

    @ConfigSection(
        name = "Focus mode",
        description = "Manual focus-mode controls",
        position = 0
    )
    String focus = "focus";

    @ConfigSection(
        name = "Optional UI",
        description = "Static UI reductions",
        position = 10
    )
    String ui = "ui";

    @ConfigItem(
        keyName = "enabled",
        name = "Enable focus mode",
        description = "Manually enable the Jad Focus Mode overlay.",
        position = 1,
        section = focus
    )
    default boolean enabled() {
        return false;
    }

    @ConfigItem(
        keyName = "toggleKey",
        name = "Toggle hotkey",
        description = "Press this key to toggle Jad Focus Mode on or off.",
        position = 2,
        section = focus
    )
    default Keybind toggleKey() {
        return new Keybind(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK);
    }

    @ConfigItem(
        keyName = "dimIntensity",
        name = "Dim intensity",
        description = "The darkness of the overlay filter behind the focus reminder.",
        position = 3,
        section = focus
    )
    default int dimIntensity() {
        return DEFAULT_DIM_INTENSITY;
    }

    @ConfigItem(
        keyName = "orbScale",
        name = "Orb scale",
        description = "Optional HP/prayer orb enlargement while focus mode is active.",
        position = 4,
        section = focus
    )
    default int orbScale() {
        return DEFAULT_ORB_SCALE;
    }

    @ConfigItem(
        keyName = "showReminderText",
        name = "Show reminder text",
        description = "Display static focus reminders for the player.",
        position = 5,
        section = focus
    )
    default boolean showReminderText() {
        return true;
    }

    @ConfigItem(
        keyName = "breathingAnimation",
        name = "Breathing animation",
        description = "Optional sinusoidal opacity pulse for the focus overlay.",
        position = 6,
        section = focus
    )
    default boolean breathingAnimation() {
        return false;
    }

    @ConfigItem(
        keyName = "showPlayerTile",
        name = "Show player tile",
        description = "Static tile outline for the player position.",
        position = 7,
        section = focus
    )
    default boolean showPlayerTile() {
        return false;
    }

    @ConfigItem(
        keyName = "showJadTile",
        name = "Show Jad tile",
        description = "Static tile outline for the Jad position.",
        position = 8,
        section = focus
    )
    default boolean showJadTile() {
        return false;
    }

    @ConfigItem(
        keyName = "showHealerReminder",
        name = "Show healer reminder",
        description = "Show a static reminder card for healer phases.",
        position = 9,
        section = focus
    )
    default boolean showHealerReminder() {
        return false;
    }

    @ConfigItem(
        keyName = "zoomOut",
        name = "Zoom out",
        description = "Optional static camera zoom reduction while focus mode is active.",
        position = 10,
        section = focus
    )
    default boolean zoomOut() {
        return false;
    }

    @ConfigItem(
        keyName = "hideChatbox",
        name = "Hide chatbox",
        description = "Hide the chatbox while using focus mode.",
        position = 1,
        section = ui
    )
    default boolean hideChatbox() {
        return false;
    }

    @ConfigItem(
        keyName = "hideInventory",
        name = "Hide inventory",
        description = "Hide the inventory panel while using focus mode.",
        position = 2,
        section = ui
    )
    default boolean hideInventory() {
        return false;
    }

    @ConfigItem(
        keyName = "hideMinimap",
        name = "Hide minimap",
        description = "Hide the minimap while using focus mode.",
        position = 3,
        section = ui
    )
    default boolean hideMinimap() {
        return false;
    }

    @ConfigItem(
        keyName = "hideXpDrops",
        name = "Hide XP drops",
        description = "Hide XP drops while using focus mode.",
        position = 4,
        section = ui
    )
    default boolean hideXpDrops() {
        return false;
    }

    @ConfigItem(
        keyName = "hideDamageSplats",
        name = "Hide damage splats",
        description = "Hide damage splats while using focus mode.",
        position = 5,
        section = ui
    )
    default boolean hideDamageSplats() {
        return false;
    }
}
