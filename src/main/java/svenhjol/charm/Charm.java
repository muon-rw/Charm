package svenhjol.charm;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(Charm.MOD_ID)
public class Charm {
    public static final String MOD_ID = "charm";

    public Charm() {
        // There is no common code so just execute the client.
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CharmClient::new);
    }
}
