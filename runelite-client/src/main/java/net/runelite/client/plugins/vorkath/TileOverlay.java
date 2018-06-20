package net.runelite.client.plugins.vorkath;

import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import java.awt.*;
import java.util.ArrayList;

public class TileOverlay extends Overlay {

    private final Client client;
    private final VorkathPlugin plugin;


    @Inject
    public TileOverlay(Client client, VorkathPlugin plugin) {
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
        this.client = client;
        this.plugin = plugin;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        ArrayList<WorldPoint> points = plugin.getPoints();

        for (WorldPoint point : points) {
            if (point.getPlane() != client.getPlane()) {
                continue;
            }
            //System.out.println("attempting to render" + point)
            drawTile(graphics, point, Color.decode("#58045C"));
        }

        return null;
    }

    private void drawTile(Graphics2D graphics, WorldPoint point, Color color) {
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();

        if (point.distanceTo(playerLocation) >= 32) {
            return;
        }

        LocalPoint lp = LocalPoint.fromWorld(client, point);
        if (lp == null) {
            return;
        }

        Polygon poly = Perspective.getCanvasTilePoly(client, lp);
        if (poly == null) {
            return;
        }

        OverlayUtil.renderPolygon(graphics, poly, color);
        //System.out.println("rendered point" + point);
    }

}