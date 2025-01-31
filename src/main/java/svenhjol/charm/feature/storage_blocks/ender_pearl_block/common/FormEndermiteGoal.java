package svenhjol.charm.feature.storage_blocks.ender_pearl_block.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.feature.storage_blocks.ender_pearl_block.EnderPearlBlock;
import svenhjol.charm.charmony.feature.FeatureResolver;
import svenhjol.charm.charmony.common.helper.MobHelper;

import java.util.EnumSet;

public class FormEndermiteGoal extends RandomStrollGoal implements FeatureResolver<EnderPearlBlock> {
    private final Silverfish silverfish;
    private Direction facing;
    private boolean merge;

    public FormEndermiteGoal(Silverfish silverfish) {
        super(silverfish, 0.6d);
        this.silverfish = silverfish;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!silverfish.getCommandSenderWorld().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return false;
        } else if (silverfish.getTarget() != null) {
            return false;
        } else if (!silverfish.getNavigation().isDone()) {
            return false;
        } else {
            var random = silverfish.getRandom();

            if (random.nextFloat() < 0.8d) {
                facing = Direction.getRandom(random);
                BlockPos pos = getPosition(silverfish).relative(facing);
                BlockState state = silverfish.getCommandSenderWorld().getBlockState(pos);

                if (state.is(feature().registers.block.get())) {
                    merge = true;
                    return true;
                }
            }

            merge = false;
            return super.canUse();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !merge && super.canContinueToUse();
    }

    @Override
    public void start() {
        var level = silverfish.getCommandSenderWorld();
        if (level.isClientSide()) return;

        var serverLevel = (ServerLevel)level;
        var pos = getPosition(silverfish);

        if (facing == null) return;

        var relative = pos.relative(facing);
        var state = level.getBlockState(relative);

        if (state.is(feature().registers.block.get())) {
            MobHelper.spawn(EntityType.ENDERMITE, serverLevel, silverfish.blockPosition(), MobSpawnType.CONVERSION, m -> {
                level.removeBlock(relative, false);
                silverfish.spawnAnim();
                silverfish.discard();
                feature().advancements.convertedSilverfish(level, pos);
            });
        }
    }

    private BlockPos getPosition(Silverfish silverfish) {
        var pos = silverfish.blockPosition();
        return new BlockPos(pos.getX(), (int) (pos.getY() + 0.5d), pos.getZ());
    }

    @Override
    public Class<EnderPearlBlock> typeForFeature() {
        return EnderPearlBlock.class;
    }
}
