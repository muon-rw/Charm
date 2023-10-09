package svenhjol.charm.feature.make_suspicious_blocks;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony_api.event.BlockUseEvent;
import svenhjol.charmony.base.CharmonyFeature;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Add an item to sand and gravel when holding a brush in your offhand.")
public class MakeSuspiciousBlocks extends CharmonyFeature {
    static final Map<Block, Block> SUSPICIOUS_BLOCK_CONVERSIONS = new HashMap<>();
    static Supplier<SoundEvent> addItemSound;

    @Override
    public void register() {
        addItemSound = Charm.instance().registry().soundEvent("make_suspicious_block");
        registerSuspiciousBlockConversion(Blocks.SAND, Blocks.SUSPICIOUS_SAND);
        registerSuspiciousBlockConversion(Blocks.GRAVEL, Blocks.SUSPICIOUS_GRAVEL);
    }

    @Override
    public void runWhenEnabled() {
        BlockUseEvent.INSTANCE.handle(this::handleBlockUse);
    }

    public static void registerSuspiciousBlockConversion(Block normal, Block suspicious) {
        SUSPICIOUS_BLOCK_CONVERSIONS.put(normal, suspicious);
    }

    private InteractionResult handleBlockUse(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        var offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        var mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!offHand.is(Items.BRUSH) || mainHand.isEmpty()) {
            return InteractionResult.PASS;
        }

        var pos = hitResult.getBlockPos();
        var normalBlock = level.getBlockState(pos).getBlock();
        var suspiciousBlock = SUSPICIOUS_BLOCK_CONVERSIONS.getOrDefault(normalBlock, null);
        if (suspiciousBlock == null) {
            return InteractionResult.PASS;
        }

        level.setBlock(pos, suspiciousBlock.defaultBlockState(), 2);

        var optional = level.getBlockEntity(pos, BlockEntityType.BRUSHABLE_BLOCK);
        if (optional.isPresent()) {
            var brushable = (BrushableBlockEntity) optional.get();
            brushable.lootTable = null;
            brushable.item = mainHand.copy();

            if (!player.getAbilities().instabuild) {
                mainHand.shrink(mainHand.getCount());
            }

            if (level.isClientSide) {
                var random = level.getRandom();
                for (int i = 0; i < 18; i++) {
                    level.addParticle(ParticleTypes.ASH,
                        pos.getX() + (random.nextDouble() * 1.25d),
                        pos.getY() + 1.08d,
                        pos.getZ() + (random.nextDouble() * 1.25d),
                        0.0d, 0.0d, 0.0d);
                }
            }

            level.playSound(null, pos, addItemSound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            player.swing(InteractionHand.OFF_HAND);
            triggerMadeSuspiciousBlock(player);
            
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    public static void triggerMadeSuspiciousBlock(Player player) {
        Advancements.trigger(Charm.instance().makeId("made_suspicious_block"), player);
    }
}
