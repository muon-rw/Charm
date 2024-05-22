package svenhjol.charm.feature.note_block_lower_pitch.common;

import svenhjol.charm.api.event.BlockUseEvent;
import svenhjol.charm.feature.note_block_lower_pitch.NoteBlockLowerPitch;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<NoteBlockLowerPitch> {
    public Registers(NoteBlockLowerPitch feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        BlockUseEvent.INSTANCE.handle(feature().handlers::useBlock);
    }
}
