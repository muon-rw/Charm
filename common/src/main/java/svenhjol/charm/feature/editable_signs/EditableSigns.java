package svenhjol.charm.feature.editable_signs;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_api.event.BlockUseEvent;
import svenhjol.charm_core.base.CharmFeature;

@Feature(mod = Charm.MOD_ID, description = "Right-click on a sign with an empty hand to edit its text.")
public class EditableSigns extends CharmFeature {
    @Override
    public void runWhenEnabled() {
        BlockUseEvent.INSTANCE.handle(this::handleBlockUse);
    }

    private InteractionResult handleBlockUse(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        var pos = hitResult.getBlockPos();
        if (!level.isClientSide()
            && player != null && player.getItemInHand(hand).isEmpty()
            && level.getBlockEntity(pos) instanceof SignBlockEntity sign) {
            sign.setEditable(true);
            player.openTextEdit(sign);
            // TODO: advancement.
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
