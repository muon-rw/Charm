package svenhjol.charm.module.kilns;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.module.kilns.KilnScreen;
import svenhjol.charm.module.kilns.Kilns;

public class KilnsClient extends CharmClientModule {
    public KilnsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        ScreenRegistry.register(Kilns.SCREEN_HANDLER, KilnScreen::new);
    }
}
