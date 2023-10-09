package svenhjol.charm.feature.piglins_follow_gold_blocks;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony_api.event.EntityJoinEvent;
import svenhjol.charmony_api.event.PlayerTickEvent;

import java.util.List;

@SuppressWarnings("UnusedReturnValue")
@Feature(mod = Charm.MOD_ID, description = "Piglins are attracted when the player holds a block of gold.")
public class PiglinsFollowGoldBlocks extends CharmonyFeature {
    @Override
    public void runWhenEnabled() {
        EntityJoinEvent.INSTANCE.handle(this::handleEntityJoin);
        PlayerTickEvent.INSTANCE.handle(this::handlePlayerTick);
    }

    private void handlePlayerTick(Player player) {
        if (!player.level().isClientSide
            && player.level().getGameTime() % 40 == 0
            && player.getMainHandItem().getItem() == Items.GOLD_BLOCK
        ) {
            List<Piglin> piglins = player.level().getEntitiesOfClass(Piglin.class, new AABB(player.blockPosition()).inflate(8.0D));
            if (!piglins.isEmpty()) {
                triggerLuredPiglin(player);
            }
        }
    }

    private InteractionResult handleEntityJoin(Entity entity, Level level) {
        if (entity instanceof Piglin piglin) {
            var ingredient = Ingredient.of(Blocks.GOLD_BLOCK);
            var goalSelector = piglin.goalSelector;

            if (goalSelector.getAvailableGoals().stream().noneMatch(g -> g.getGoal() instanceof TemptGoal)) {
                goalSelector.addGoal(3, new TemptGoal(piglin, 0.6, ingredient, false));
            }
        }

        return InteractionResult.PASS;
    }

    public static void triggerLuredPiglin(Player player) {
        Advancements.trigger(Charm.instance().makeId("lured_piglin"), player);
    }
}
