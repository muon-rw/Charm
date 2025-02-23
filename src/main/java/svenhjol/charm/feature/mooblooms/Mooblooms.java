package svenhjol.charm.feature.mooblooms;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.mooblooms.common.Advancements;
import svenhjol.charm.feature.mooblooms.common.Handlers;
import svenhjol.charm.feature.mooblooms.common.Registers;

@Feature(description = """
    Mooblooms are cow-like mobs that come in a variety of flower types.
    They spawn flowers where they walk and can be milked for suspicious stew.""")
public final class Mooblooms extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    public Mooblooms(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}
