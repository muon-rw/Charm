package svenhjol.charm_core.base;

import net.minecraft.world.item.Item;

public abstract class CharmItem extends Item {
    protected final CharmFeature feature;
    protected final Properties properties;

    public CharmItem(CharmFeature feature, Properties properties) {
        super(properties);
        this.properties = properties;
        this.feature = feature;
    }

    public boolean isEnabled() {
        return feature.isEnabled();
    }
}
