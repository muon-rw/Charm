package svenhjol.charm_api.event;

import net.minecraft.world.entity.player.Player;

public class PlayerTickEvent extends CharmEvent<PlayerTickEvent.Handler> {
    public static final PlayerTickEvent INSTANCE = new PlayerTickEvent();

    private PlayerTickEvent() {}

    public void invoke(Player player) {
        for (Handler handler : getHandlers()) {
            handler.run(player);
        }
    }

    @FunctionalInterface
    public interface Handler {
        void run(Player player);
    }
}
