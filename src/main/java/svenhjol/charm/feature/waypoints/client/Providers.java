package svenhjol.charm.feature.waypoints.client;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.api.iface.CompassOverlayItem;
import svenhjol.charm.api.iface.CompassOverlayProvider;
import svenhjol.charm.charmony.feature.ProviderHolder;
import svenhjol.charm.feature.waypoints.WaypointsClient;

import java.util.List;

public final class Providers extends ProviderHolder<WaypointsClient> implements CompassOverlayProvider {
    public Providers(WaypointsClient feature) {
        super(feature);
    }

    @Override
    public List<CompassOverlayItem> getCompassOverlayItems() {
        return List.of(
            new CompassOverlayItem() {
                @Override
                public boolean shouldShow(Player player) {
                    return feature().linked().showNearestWaypointOnCompass()
                        && feature().handlers.isPlayerInRange();
                }

                @Override
                public String text() {
                    var title = feature().handlers.title().orElse("");
                    var strengthMeter = feature().handlers.strengthMeter();
                    return Component.translatable("gui.charm.compass.closest_waypoint", title, strengthMeter).getString();
                }

                @Override
                public int color() {
                    return feature().handlers.color()
                        .map(dyeColor -> dyeColor.getFireworkColor() | 0x303030)
                        .orElse(0xffffff);
                }
            }
        );
    }
}
