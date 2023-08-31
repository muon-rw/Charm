package svenhjol.charm.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import svenhjol.charm.Charm;

@Mod(Charm.MOD_ID)
public class Initializer {
    public Initializer() {
        // There is no common code so just execute the client.
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientInitializer::new);
    }
}
