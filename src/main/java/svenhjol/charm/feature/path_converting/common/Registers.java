package svenhjol.charm.feature.path_converting.common;

import net.minecraft.sounds.SoundEvent;
import svenhjol.charm.charmony.event.BlockUseEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.path_converting.PathConverting;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<PathConverting> {
    public final Supplier<SoundEvent> dirtToPathSound;
    public final Supplier<SoundEvent> pathToDirtSound;

    public Registers(PathConverting feature) {
        super(feature);

        var registry = feature().registry();
        dirtToPathSound = registry.soundEvent("dirt_to_path");
        pathToDirtSound = registry.soundEvent("path_to_dirt");
    }

    @Override
    public void onEnabled() {
        BlockUseEvent.INSTANCE.handle(feature().handlers::useBlock);
    }
}
