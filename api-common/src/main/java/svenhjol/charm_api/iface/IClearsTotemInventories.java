package svenhjol.charm_api.iface;

import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.function.Consumer;

public interface IClearsTotemInventories {
    List<Consumer<Player>> clearTotemInventories();
}
