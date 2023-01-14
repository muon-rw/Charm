package svenhjol.charm.feature.clear_item_frames;

import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.function.Supplier;

@ClientFeature
public class ClearItemFramesClient extends CharmFeature {
    static Supplier<SpriteParticleRegistration<SimpleParticleType>> PARTICLE;

    @Override
    public void register() {
        PARTICLE = CharmClient.REGISTRY.particle(ClearItemFrames.PARTICLE_TYPE,
            () -> ApplyAmethystClientParticle::new);

        addDependencyCheck(m -> Charm.LOADER.isEnabled(ClearItemFrames.class));
    }

    static void handleItemFrameInteraction(ClearItemFramesNetwork.IItemFrameInteraction message, Player player) {
        var pos = message.getPos();
        var sound = message.getSound();
        player.level.playSound(player, pos, sound, SoundSource.PLAYERS, 1.0F, 1.0F);

        for (int i = 0; i < 3; i++) {
            ClearItemFramesClient.createParticle(player.level, pos);
        }
    }

    static void createParticle(Level level, BlockPos pos) {
        var particleType = ClearItemFrames.PARTICLE_TYPE.get();

        float[] col = DyeColor.PURPLE.getTextureDiffuseColors();
        var x = (double) pos.getX() + 0.5D;
        var y = (double) pos.getY() + 0.5D;
        var z = (double) pos.getZ() + 0.5D;

        level.addAlwaysVisibleParticle(particleType, x, y, z, col[0], col[1], col[2]);
    }
}
