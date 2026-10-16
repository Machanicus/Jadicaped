package com.jadicaped;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.NoninvertibleTransformException;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class JadicapedOverlay extends OverlayPanel {
    private static final int OVERLAY_WIDTH = 280;
    private static final int OVERLAY_ALPHA = 180;
    private static final int MAX_DIM_ALPHA = 100;
    private static final String JAD_NAME = "TzTok-Jad";

    private final JadicapedPlugin plugin;
    private final JadicapedConfig config;

    @Inject
    private JadicapedOverlay(JadicapedPlugin plugin, JadicapedConfig config) {
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.TOP_LEFT);
        setPriority(OverlayPriority.HIGH);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!plugin.isFocusModeActive()) {
            return null;
        }

        final Client client = plugin.getClient();
        final Rectangle canvas = new Rectangle(client.getCanvasWidth(), client.getCanvasHeight());
        if (canvas.isEmpty()) {
            return null;
        }

        if (!client.isMenuOpen()) {
            final Area dimArea = new Area(canvas);
            subtractActor(dimArea, client.getLocalPlayer());
            for (NPC npc : client.getNpcs()) {
                if (JAD_NAME.equals(npc.getName())) {
                    subtractActor(dimArea, npc);
                }
            }

            subtractWidgets(dimArea, client,
                WidgetInfo.MINIMAP_ORBS,
                WidgetInfo.MINIMAP_HEALTH_ORB,
                WidgetInfo.MINIMAP_PRAYER_ORB,
                WidgetInfo.MINIMAP_PRAYER_ORB_TEXT,
                WidgetInfo.MINIMAP_QUICK_PRAYER_ORB,
                WidgetInfo.FIXED_VIEWPORT_MINIMAP,
                WidgetInfo.RESIZABLE_MINIMAP_WIDGET,
                WidgetInfo.RESIZABLE_MINIMAP_STONES_WIDGET,
                WidgetInfo.RESIZABLE_MINIMAP_STONES_ORB_HOLDER
            );

            if (client.isResized()) {
                subtractWidgets(dimArea, client,
                    WidgetInfo.RESIZABLE_VIEWPORT_INVENTORY_CONTAINER,
                    WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_INTERFACE_CONTAINER,
                    WidgetInfo.RESIZABLE_VIEWPORT_INTERFACE_CONTAINER,
                    WidgetInfo.RESIZABLE_VIEWPORT_COMBAT_TAB,
                    WidgetInfo.RESIZABLE_VIEWPORT_STATS_TAB,
                    WidgetInfo.RESIZABLE_VIEWPORT_QUESTS_TAB,
                    WidgetInfo.RESIZABLE_VIEWPORT_INVENTORY_TAB,
                    WidgetInfo.RESIZABLE_VIEWPORT_EQUIPMENT_TAB,
                    WidgetInfo.RESIZABLE_VIEWPORT_PRAYER_TAB,
                    WidgetInfo.RESIZABLE_VIEWPORT_MAGIC_TAB,
                    WidgetInfo.RESIZABLE_VIEWPORT_FRIENDS_CHAT_TAB,
                    WidgetInfo.RESIZABLE_VIEWPORT_FRIENDS_TAB,
                    WidgetInfo.RESIZABLE_VIEWPORT_IGNORES_TAB,
                    WidgetInfo.RESIZABLE_VIEWPORT_LOGOUT_TAB,
                    WidgetInfo.RESIZABLE_VIEWPORT_OPTIONS_TAB,
                    WidgetInfo.RESIZABLE_VIEWPORT_EMOTES_TAB,
                    WidgetInfo.RESIZABLE_VIEWPORT_MUSIC_TAB,
                    WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_TABS1,
                    WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_TABS2
                );
            } else {
                subtractWidgets(dimArea, client,
                    WidgetInfo.FIXED_VIEWPORT_COMBAT_TAB,
                    WidgetInfo.FIXED_VIEWPORT_STATS_TAB,
                    WidgetInfo.FIXED_VIEWPORT_QUESTS_TAB,
                    WidgetInfo.FIXED_VIEWPORT_INVENTORY_TAB,
                    WidgetInfo.FIXED_VIEWPORT_EQUIPMENT_TAB,
                    WidgetInfo.FIXED_VIEWPORT_PRAYER_TAB,
                    WidgetInfo.FIXED_VIEWPORT_MAGIC_TAB,
                    WidgetInfo.FIXED_VIEWPORT_FRIENDS_CHAT_TAB,
                    WidgetInfo.FIXED_VIEWPORT_FRIENDS_TAB,
                    WidgetInfo.FIXED_VIEWPORT_IGNORES_TAB,
                    WidgetInfo.FIXED_VIEWPORT_LOGOUT_TAB,
                    WidgetInfo.FIXED_VIEWPORT_OPTIONS_TAB,
                    WidgetInfo.FIXED_VIEWPORT_EMOTES_TAB,
                    WidgetInfo.FIXED_VIEWPORT_MUSIC_TAB
                );
            }

            try {
                final AffineTransform toLocal = graphics.getTransform().createInverse();
                final Shape localDimArea = toLocal.createTransformedShape(dimArea);
                final Graphics2D dimGraphics = (Graphics2D) graphics.create();
                dimGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                dimGraphics.setColor(new Color(0, 0, 0, Math.max(0, Math.min(config.dimIntensity(), MAX_DIM_ALPHA))));
                dimGraphics.fill(localDimArea);
                dimGraphics.dispose();
            } catch (NoninvertibleTransformException ex) {
                return null;
            }
        }

        final PanelComponent panelComponent = new PanelComponent();
        panelComponent.setPreferredSize(new Dimension(OVERLAY_WIDTH, 0));
        panelComponent.setBackgroundColor(new Color(0, 0, 0, OVERLAY_ALPHA));

        panelComponent.getChildren().add(LineComponent.builder()
            .left("Jad Focus Mode")
            .right("Manual")
            .build());

        if (config.showReminderText()) {
            panelComponent.getChildren().add(LineComponent.builder()
                .left("Focus")
                .right("Breathe and stay calm")
                .build());
        }

        return panelComponent.render(graphics);
    }

    private void subtractActor(Area dimArea, Actor actor) {
        if (actor == null) {
            return;
        }

        final Shape hull = actor.getConvexHull();
        if (hull != null) {
            dimArea.subtract(new Area(hull));
        }
    }

    private void subtractWidget(Area dimArea, Client client, WidgetInfo widgetInfo) {
        final Widget widget = client.getWidget(widgetInfo);
        if (widget != null && !widget.isHidden()) {
            final Rectangle bounds = widget.getBounds();
            if (!bounds.isEmpty()) {
                bounds.grow(3, 3);
                dimArea.subtract(new Area(bounds));
            }
        }
    }

    private void subtractWidgets(Area dimArea, Client client, WidgetInfo... widgetInfos) {
        for (WidgetInfo widgetInfo : widgetInfos) {
            subtractWidget(dimArea, client, widgetInfo);
        }
    }
}
