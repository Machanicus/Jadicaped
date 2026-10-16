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
    int DEFAULT_DIM_INTENSITY = 45;

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
        keyName = "showReminderText",
        name = "Show reminder text",
        description = "Display a neutral focus reminder.",
        position = 4,
        section = focus
    )
    default boolean showReminderText() {
        return true;
    }

    @ConfigItem(
        keyName = "hideChatbox",
        name = "Hide chat messages",
        description = "Hide chat message lines while focus mode is active.",
        position = 1,
        section = ui
    )
    default boolean hideChatbox() {
        return false;
    }

}
