package com.jadicaped;

import com.google.inject.Provides;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
    name = "Jad Focus Mode",
    description = "Gently dims the client while keeping Jad, the player, minimap, and interface controls clear.",
    tags = {"jad", "fight cave", "osrs", "overlay", "focus"}
)
public class JadicapedPlugin extends Plugin implements KeyListener {
    private static final int FIGHT_CAVES_REGION_ID = 9551;
    private static final String CONFIG_GROUP = "jadicaped";
    private static final int SUMMARY_PRIORITY = 20;
    private static final int ICON_SIZE = 32;
    private static final int ICON_PADDING = 5;
    private static final int ICON_PANEL_SIZE = 22;
    private static final int ICON_CORNER_RADIUS = 6;
    private static final int ICON_DARK_RED = 31;
    private static final int ICON_DARK_GREEN = 31;
    private static final int ICON_DARK_BLUE = 31;
    private static final int ICON_DARK_ALPHA = 220;
    private static final int ICON_LIGHT_RED = 117;
    private static final int ICON_LIGHT_GREEN = 211;
    private static final int ICON_LIGHT_BLUE = 255;
    private static final int ICON_LINE_START = 9;
    private static final int ICON_LINE_MIDDLE = 15;
    private static final int ICON_LINE_END = 21;
    private static final int ICON_LINE_X_END = 23;
    private static final int JAD_WAVE = 63;
    private static final int PRE_JAD_WAVE = 62;
    private static final int JAD_REMINDER_DURATION_TICKS = 15;
    private static final int NO_JAD_REMINDER_TICK = 0;
    private static final Pattern FIGHT_CAVES_WAVE_MESSAGE = Pattern.compile("Wave: (\\d+)");

    @Inject
    private Client client;

    @Inject
    private JadicapedConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private JadicapedOverlay overlay;

    @Inject
    private PostDeathSummaryPanel postDeathSummaryPanel;

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private KeyManager keyManager;

    private NavigationButton postDeathSummaryButton;
    private boolean focusModeActive;
    private boolean insideFightCaves;
    private Boolean manualFocusOverride;
    private int jadLogoutReminderExpiresAt = NO_JAD_REMINDER_TICK;

    @Provides
    JadicapedConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(JadicapedConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        insideFightCaves = isInFightCaves();
        keyManager.registerKeyListener(this);
        overlayManager.add(overlay);
        refreshFocusMode();
        syncSummaryPanel();
    }

    @Override
    protected void shutDown() throws Exception {
        keyManager.unregisterKeyListener(this);
        overlayManager.remove(overlay);
        applyUiVisibility(false);
        focusModeActive = false;
        manualFocusOverride = null;
        unregisterSummaryPanel();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (config.toggleKey().matches(e)) {
            manualFocusOverride = !focusModeActive;
            refreshFocusMode();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Subscribe
    private void onGameTick(GameTick event) {
        final boolean nowInFightCaves = isInFightCaves();
        if (nowInFightCaves != insideFightCaves) {
            insideFightCaves = nowInFightCaves;
            manualFocusOverride = null;
            if (!insideFightCaves) {
                jadLogoutReminderExpiresAt = NO_JAD_REMINDER_TICK;
            }
        }
        refreshFocusMode();
    }

    @Subscribe
    private void onChatMessage(ChatMessage event) {
        if (!insideFightCaves || event.getType() != ChatMessageType.GAMEMESSAGE) {
            return;
        }

        final Matcher waveMatcher = FIGHT_CAVES_WAVE_MESSAGE.matcher(event.getMessage());
        if (!waveMatcher.find()) {
            return;
        }

        final int wave = Integer.parseInt(waveMatcher.group(1));
        if (wave == PRE_JAD_WAVE && config.showJadLogoutReminder()) {
            jadLogoutReminderExpiresAt = client.getTickCount() + JAD_REMINDER_DURATION_TICKS;
        } else if (wave >= JAD_WAVE) {
            jadLogoutReminderExpiresAt = NO_JAD_REMINDER_TICK;
        }
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event) {
        if (!CONFIG_GROUP.equals(event.getGroup())) {
            return;
        }

        final String key = event.getKey();
        if ("showSummaryPanel".equals(key)) {
            syncSummaryPanel();
        } else if ("hideChatbox".equals(key)) {
            applyUiVisibility(focusModeActive);
        } else if ("enabled".equals(key) || "autoFocusInFightCaves".equals(key)) {
            manualFocusOverride = null;
            refreshFocusMode();
        }
    }

    public boolean isFocusModeActive() {
        return focusModeActive;
    }

    public Client getClient() {
        return client;
    }

    public JadicapedConfig getConfig() {
        return config;
    }

    public boolean isJadLogoutReminderActive() {
        return config.showJadLogoutReminder()
            && insideFightCaves
            && client.getTickCount() < jadLogoutReminderExpiresAt;
    }

    public void updatePostDeathSummary(PostDeathSummary summary) {
        if (postDeathSummaryPanel != null) {
            postDeathSummaryPanel.setSummary(summary);
        }
    }

    public void resetPostDeathSummary() {
        if (postDeathSummaryPanel != null) {
            postDeathSummaryPanel.setNoAttemptYet();
        }
    }

    private boolean isInFightCaves() {
        final int[] mapRegions = client.getMapRegions();
        return client.getGameState() == GameState.LOGGED_IN
            && mapRegions != null
            && Arrays.stream(mapRegions).anyMatch(regionId -> regionId == FIGHT_CAVES_REGION_ID);
    }

    private void refreshFocusMode() {
        final boolean shouldBeActive = manualFocusOverride != null
            ? manualFocusOverride
            : insideFightCaves ? config.autoFocusInFightCaves() : config.enabled();

        if (shouldBeActive != focusModeActive) {
            focusModeActive = shouldBeActive;
            applyUiVisibility(focusModeActive);
        }
    }

    private void syncSummaryPanel() {
        if (config.showSummaryPanel()) {
            registerSummaryPanel();
        } else {
            unregisterSummaryPanel();
        }
    }

    private void registerSummaryPanel() {
        if (postDeathSummaryButton != null) {
            return;
        }

        postDeathSummaryButton = NavigationButton.builder()
            .tooltip("Post-Death Sidebar Summary")
            .priority(SUMMARY_PRIORITY)
            .icon(createSummaryIcon())
            .panel(postDeathSummaryPanel)
            .build();

        clientToolbar.addNavigation(postDeathSummaryButton);
    }

    private void unregisterSummaryPanel() {
        if (postDeathSummaryButton != null) {
            clientToolbar.removeNavigation(postDeathSummaryButton);
            postDeathSummaryButton = null;
        }
    }

    private BufferedImage createSummaryIcon() {
        final BufferedImage image = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(new Color(ICON_DARK_RED, ICON_DARK_GREEN, ICON_DARK_BLUE, ICON_DARK_ALPHA));
        graphics.fillRoundRect(ICON_PADDING, ICON_PADDING, ICON_PANEL_SIZE, ICON_PANEL_SIZE, ICON_CORNER_RADIUS,
            ICON_CORNER_RADIUS);
        graphics.setColor(new Color(ICON_LIGHT_RED, ICON_LIGHT_GREEN, ICON_LIGHT_BLUE, 255));
        graphics.setStroke(new BasicStroke(2f));
        graphics.drawLine(ICON_LINE_START, ICON_LINE_START, ICON_LINE_X_END, ICON_LINE_START);
        graphics.drawLine(ICON_LINE_START, ICON_LINE_MIDDLE, ICON_LINE_X_END, ICON_LINE_MIDDLE);
        graphics.drawLine(ICON_LINE_START, ICON_LINE_END, ICON_LINE_X_END, ICON_LINE_END);
        graphics.dispose();
        return image;
    }

    private void applyUiVisibility(boolean active) {
        if (!active) {
            hideWidget(WidgetInfo.CHATBOX_MESSAGE_LINES, false);
            return;
        }

        hideWidget(WidgetInfo.CHATBOX_MESSAGE_LINES, config.hideChatbox());
    }

    private void hideWidget(WidgetInfo widgetInfo, boolean hide) {
        final Widget widget = client.getWidget(widgetInfo);
        if (widget != null) {
            widget.setHidden(hide);
        }
    }
}
