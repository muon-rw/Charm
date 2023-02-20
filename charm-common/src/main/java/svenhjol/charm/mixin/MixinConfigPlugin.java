package svenhjol.charm.mixin;

import svenhjol.charm.Charm;
import svenhjol.charm_core.base.BaseMixinConfigPlugin;

public class MixinConfigPlugin extends BaseMixinConfigPlugin {
    @Override
    protected String getModId() {
        return Charm.MOD_ID;
    }
}
