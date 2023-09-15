package svenhjol.charm.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import svenhjol.charm.Charm;

@Mod(Charm.MOD_ID)
public class Initializer {
    public Initializer() {
        var charm = Charm.instance();
        var loader = charm.loader();

        // Autoload all annotated features from the feature namespace.
        loader.init(charm.featurePrefix(), charm.featureAnnotation());

        // Execute client init so that client registration happens.
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientInitializer::new);
    }
}