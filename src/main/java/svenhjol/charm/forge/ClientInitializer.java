package svenhjol.charm.forge;

import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.base.Mods;

public class ClientInitializer {
    public ClientInitializer() {
        var instance = Mods.client(Charm.ID, CharmClient::new);
        var loader = instance.loader();

        // Autoload all annotated client features from the feature namespace.
        loader.init(instance.features());
    }
}
