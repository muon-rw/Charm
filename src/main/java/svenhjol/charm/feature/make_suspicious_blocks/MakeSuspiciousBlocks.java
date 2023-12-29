package svenhjol.charm.feature.make_suspicious_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import svenhjol.charm.Charm;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.PlayerHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MakeSuspiciousBlocks extends CommonFeature {
    static final Map<Block, Block> SUSPICIOUS_BLOCK_CONVERSIONS = new HashMap<>();
    static Supplier<SoundEvent> addItemSound;

    @Override
    public String description() {
        return "Use a piston to push an item into sand or gravel, making it suspicious.";
    }

    @Override
    public void register() {
        addItemSound = mod().registry().soundEvent("make_suspicious_block");
        registerSuspiciousBlockConversion(Blocks.SAND, Blocks.SUSPICIOUS_SAND);
        registerSuspiciousBlockConversion(Blocks.GRAVEL, Blocks.SUSPICIOUS_GRAVEL);
    }

    public static void registerSuspiciousBlockConversion(Block normal, Block suspicious) {
        SUSPICIOUS_BLOCK_CONVERSIONS.put(normal, suspicious);
    }

    /**
     * Called by the piston base block mixin.
     * Ensure the block two spaces from the piston head direction is able to be converted.
     * Check for item entities in the airspace that the piston head pushes into.
     */
    public static void checkAndConvert(Level level, BlockPos pos, BlockState state) {
        var direction = state.getValue(PistonBaseBlock.FACING);
        var d1 = pos.relative(direction, 1);
        var d2 = pos.relative(direction, 2);
        var target = level.getBlockState(d2);
        var random = level.getRandom();

        if (!SUSPICIOUS_BLOCK_CONVERSIONS.containsKey(target.getBlock())) {
            return;
        }

        var itemEntities = level.getEntitiesOfClass(ItemEntity.class, new AABB(d1));
        if (itemEntities.isEmpty()) {
            return;
        }

        // Get one of the stacks at random
        var itemEntity = itemEntities.get(random.nextInt(itemEntities.size()));
        var stack = itemEntity.getItem();
        var result = makeSuspiciousBlock(level, d2, stack);

        if (result) {
            itemEntity.kill();

            // Do advancement for nearby players
            if (!level.isClientSide) {
                triggerMadeSuspiciousBlock((ServerLevel) level, pos);
            }
        }
    }

    static boolean makeSuspiciousBlock(Level level, BlockPos pos, ItemStack stack) {
        var targetState = level.getBlockState(pos);
        var targetBlock = targetState.getBlock();

        var suspiciousBlock = SUSPICIOUS_BLOCK_CONVERSIONS.getOrDefault(targetBlock, null);
        if (suspiciousBlock == null) {
            return false;
        }

        level.setBlockAndUpdate(pos, suspiciousBlock.defaultBlockState());

        var opt = level.getBlockEntity(pos, BlockEntityType.BRUSHABLE_BLOCK);
        if (opt.isPresent()) {
            var brushable = (BrushableBlockEntity) opt.get();
            brushable.lootTable = null;
            brushable.item = stack.copy();

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
            return true;
        }

        return false;
    }

    public static void triggerMadeSuspiciousBlock(ServerLevel level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 8.0d).forEach(
            player -> Advancements.trigger(new ResourceLocation(Charm.ID, "made_suspicious_block"), player));
    }
}
