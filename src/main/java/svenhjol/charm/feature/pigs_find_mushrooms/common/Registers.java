package svenhjol.charm.feature.pigs_find_mushrooms.common;

import net.minecraft.sounds.SoundEvent;
import svenhjol.charm.api.event.EntityJoinEvent;
import svenhjol.charm.feature.pigs_find_mushrooms.PigsFindMushrooms;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<PigsFindMushrooms> {
    public final Supplier<SoundEvent> sniffingSound;

    public Registers(PigsFindMushrooms feature) {
        super(feature);

        sniffingSound = feature().registry().soundEvent("pig_sniffing");
    }

    @Override
    public void onEnabled() {
        EntityJoinEvent.INSTANCE.handle(feature().handlers::entityJoin);
    }
}