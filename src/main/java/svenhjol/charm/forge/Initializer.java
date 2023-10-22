package svenhjol.charm.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import svenhjol.charm.Charm;
import svenhjol.charmony.base.Mods;

@Mod(Charm.ID)
public class Initializer {
    public Initializer() {
        var instance = Mods.common(Charm.ID, Charm::new);
        var loader = instance.loader();

        // Autoload all annotated features from the feature namespace.
        loader.init(instance.features());

        // Execute client init so that client registration happens.
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientInitializer::new);
    }
}
