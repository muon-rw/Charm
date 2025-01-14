package svenhjol.charm.feature.animal_reviving.common;

import net.minecraft.core.component.DataComponentType;
import svenhjol.charm.charmony.event.EntityKilledEvent;
import svenhjol.charm.charmony.event.ItemUseEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.animal_reviving.AnimalReviving;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<AnimalReviving> {
    public final Supplier<DataComponentType<Data>> data;

    public Registers(AnimalReviving feature) {
        super(feature);

        data = feature().registry().dataComponent("revived_animal",
            () -> builder -> builder
                .persistent(Data.CODEC)
                .networkSynchronized(Data.STREAM_CODEC));
    }

    public DataComponentType<Data> data() {
        return data.get();
    }

    @Override
    public void onEnabled() {
        EntityKilledEvent.INSTANCE.handle(feature().handlers::entityKilled);
        ItemUseEvent.INSTANCE.handle(feature().handlers::itemUsed);
    }
}
