package svenhjol.charm_core.mixin;

import svenhjol.charm_core.CharmCore;
import svenhjol.charm_core.base.BaseMixinConfigPlugin;

public class MixinConfigPlugin extends BaseMixinConfigPlugin {
    @Override
    protected String getModId() {
        return CharmCore.MOD_ID;
    }
}