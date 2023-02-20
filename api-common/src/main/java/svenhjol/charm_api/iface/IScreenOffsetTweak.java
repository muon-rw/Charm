package svenhjol.charm_api.iface;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.screens.Screen;

public interface IScreenOffsetTweak {
    Class<? extends Screen> getScreen();

    Pair<Integer, Integer> getOffset();
}
