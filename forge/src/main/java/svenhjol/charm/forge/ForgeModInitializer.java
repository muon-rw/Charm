package svenhjol.charm.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import svenhjol.charm.Charm;

@Mod(Charm.MOD_ID)
public class ForgeModInitializer {
    public ForgeModInitializer() {
        // Just run the client init.
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ForgeClientModInitializer::new);
    }
}
