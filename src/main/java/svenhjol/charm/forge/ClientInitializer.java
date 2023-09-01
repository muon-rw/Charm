package svenhjol.charm.forge;

import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;

public class ClientInitializer {
    public ClientInitializer() {
        var client = CharmClient.instance();
        var loader = client.loader();

        // Autoload all annotated client features from the feature namespace.
        loader.init(client.featurePrefix(), ClientFeature.class);
    }
}
