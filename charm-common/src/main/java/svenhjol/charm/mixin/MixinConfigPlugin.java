package svenhjol.charm.mixin;

import svenhjol.charm.CharmClient;
import svenhjol.charm_core.base.BaseMixinConfigPlugin;

public class MixinConfigPlugin extends BaseMixinConfigPlugin {
    @Override
    protected String getModId() {
        return CharmClient.MOD_ID;
    }
}
