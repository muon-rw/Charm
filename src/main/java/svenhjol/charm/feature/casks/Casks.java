package svenhjol.charm.feature.casks;

import net.minecraft.util.Mth;
import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.casks.common.*;

@Feature(description = """
   Casks are used to hold potions. Use a potion bottle on a cask to add the bottle's contents to the cask.
   Use an empty bottle to retrieve one potion bottle back from the cask.
   Potion effects are combined inside the cask and the contents are not lost when the cask is broken.""")
public final class Casks extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Networking networking;
    public final Advancements advancements;

    @Configurable(name = "Maximum bottles", description = "Maximum number of bottles a cask can hold.")
    private static int maxBottles = 16;

    @Configurable(name = "Allow splash and lingering", description = "If true, splash and lingering potions may be added to a cask.")
    private static boolean allowSplashAndLingering = false;

    public Casks(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        networking = new Networking(this);
        advancements = new Advancements(this);
    }

    public boolean allowSplashAndLingering() {
        return allowSplashAndLingering;
    }

    public int maxBottles() {
        return Mth.clamp(maxBottles, 1, 1000);
    }
}
