package svenhjol.charm.mixin;

import svenhjol.charm.Charm;

public class MixinConfigPlugin extends svenhjol.charm_core.mixin.MixinConfigPlugin {
    @Override
    protected String getModId() {
        return Charm.MOD_ID;
    }
}
