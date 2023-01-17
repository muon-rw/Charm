package svenhjol.charm.mixin.amethyst_note_block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.amethyst_note_block.AmethystNoteBlock;

import java.util.Locale;

@Mixin(NoteBlockInstrument.class)
public class NoteBlockInstrumentMixin {
    /**
     * Check the block under the note block and return the correct sound.
     */
    @Inject(
        method = "byStateBelow",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookByState(BlockState state, CallbackInfoReturnable<NoteBlockInstrument> cir) {
        if (state.is(Blocks.AMETHYST_BLOCK)) {
            cir.setReturnValue(NoteBlockInstrument
                .valueOf(AmethystNoteBlock.NOTE_BLOCK_ID.toUpperCase(Locale.ROOT)));
        }
    }
}
