package com.jadicaped;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class JadicapedOverlay extends OverlayPanel {
    private static final int OVERLAY_WIDTH = 280;
    private static final int OVERLAY_ALPHA = 180;
    private static final int TILE_SIZE = 80;
    private static final int DEFAULT_WIDTH = 1280;
    private static final int DEFAULT_HEIGHT = 720;
    private static final double BREATHING_MIN = 0.70;
    private static final double BREATHING_DELTA = 0.30;
    private static final double BREATHING_PERIOD_MS = 500.0;
    private static final int MAX_DIM_ALPHA = 200;
    private static final int PLAYER_TILE_X = 160;
    private static final int PLAYER_TILE_Y = 120;
    private static final int JAD_TILE_X = 200;
    private static final int JAD_TILE_Y = 120;
    private static final int PLAYER_TILE_RED = 0;
    private static final int PLAYER_TILE_GREEN = 255;
    private static final int PLAYER_TILE_BLUE = 140;
    private static final int PLAYER_TILE_ALPHA = 180;
    private static final int JAD_TILE_RED = 255;
    private static final int JAD_TILE_GREEN = 70;
    private static final int JAD_TILE_BLUE = 70;
    private static final int JAD_TILE_ALPHA = 180;

    private final JadicapedPlugin plugin;
    private final JadicapedConfig config;

    @Inject
    private JadicapedOverlay(JadicapedPlugin plugin, JadicapedConfig config) {
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.TOP_LEFT);
        setPriority(OverlayPriority.HIGH);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!plugin.isFocusModeActive()) {
            return null;
        }

        final Rectangle bounds = graphics.getClipBounds();
        final int width = bounds != null ? bounds.width : DEFAULT_WIDTH;
        final int height = bounds != null ? bounds.height : DEFAULT_HEIGHT;
        final Graphics2D dimGraphics = (Graphics2D) graphics.create();
        dimGraphics.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        int alpha = config.dimIntensity();
        if (config.breathingAnimation()) {
            alpha = (int) (
                alpha * (
                    BREATHING_MIN + BREATHING_DELTA
                    * Math.sin(System.currentTimeMillis() / BREATHING_PERIOD_MS)
                )
            );
        }
        dimGraphics.setColor(new Color(0, 0, 0, Math.min(alpha, MAX_DIM_ALPHA)));
        dimGraphics.fillRect(0, 0, width, height);
        dimGraphics.dispose();

        if (config.showPlayerTile()) {
            graphics.setColor(new Color(PLAYER_TILE_RED, PLAYER_TILE_GREEN, PLAYER_TILE_BLUE, PLAYER_TILE_ALPHA));
            graphics.drawRect(PLAYER_TILE_X, PLAYER_TILE_Y, TILE_SIZE, TILE_SIZE);
        }
        if (config.showJadTile()) {
            graphics.setColor(new Color(JAD_TILE_RED, JAD_TILE_GREEN, JAD_TILE_BLUE, JAD_TILE_ALPHA));
            graphics.drawRect(width - JAD_TILE_X, JAD_TILE_Y, TILE_SIZE, TILE_SIZE);
        }

        final PanelComponent panelComponent = new PanelComponent();
        panelComponent.setPreferredSize(new Dimension(OVERLAY_WIDTH, 0));
        panelComponent.setBackgroundColor(new Color(0, 0, 0, OVERLAY_ALPHA));

        panelComponent.getChildren().add(LineComponent.builder()
            .left("Jad Focus Mode")
            .right("Manual")
            .build());

        panelComponent.getChildren().add(LineComponent.builder()
            .left("Magic")
            .right("Fireball")
            .build());

        panelComponent.getChildren().add(LineComponent.builder()
            .left("Range")
            .right("Stomp")
            .build());

        panelComponent.getChildren().add(LineComponent.builder()
            .left("Hands")
            .right("Watch only")
            .build());

        if (config.showHealerReminder()) {
            panelComponent.getChildren().add(LineComponent.builder()
                .left("Healers")
                .right("Static reminder")
                .build());
        }

        if (config.showReminderText()) {
            panelComponent.getChildren().add(LineComponent.builder()
                .left("Focus")
                .right("Breathe and stay calm")
                .build());
        }

        return panelComponent.render(graphics);
    }
}
