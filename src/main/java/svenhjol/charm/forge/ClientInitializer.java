package svenhjol.charm.forge;

import svenhjol.charm.CharmClient;

public class ClientInitializer {
    public ClientInitializer() {
        var client = CharmClient.instance();
        var loader = client.loader();

        // Autoload all annotated client features from the feature namespace.
        loader.init(client.featurePrefix(), client.featureAnnotation());
    }
}
