package svenhjol.charm.module.villagers_follow_emerald_blocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.api.event.AddEntityCallback;
import svenhjol.charm.api.event.PlayerTickCallback;
import svenhjol.charm.lib.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;

import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Villagers are attracted when the player holds a block of emeralds.")
public class VillagersFollowEmeraldBlocks extends CharmModule {
    public static final ResourceLocation TRIGGER_LURED_VILLAGER = new ResourceLocation(Charm.MOD_ID, "lured_villager");

    @Override
    public void runWhenEnabled() {
        AddEntityCallback.EVENT.register(this::followEmerald);
        PlayerTickCallback.EVENT.register(this::handlePlayerTick);
    }

    private InteractionResult followEmerald(Entity entity) {
        if (entity instanceof Villager villager) {
            Ingredient ingredient = Ingredient.of(Blocks.EMERALD_BLOCK);

            if (villager.goalSelector.getAvailableGoals().stream().noneMatch(g -> g.getGoal() instanceof TemptGoal)) {
                villager.goalSelector.addGoal(3, new TemptGoal(villager, 0.6, ingredient, false));
            }
        }

        return InteractionResult.PASS;
    }

    private void handlePlayerTick(Player player) {
        if (!player.level.isClientSide
            && player.level.getGameTime() % 40 == 0
            && player.getMainHandItem().getItem() == Items.EMERALD_BLOCK
        ) {
            List<Villager> villagers = player.level.getEntitiesOfClass(Villager.class, new AABB(player.blockPosition()).inflate(8.0D));
            if (!villagers.isEmpty()) {
                triggerLuredVillager((ServerPlayer) player);
            }
        }
    }

    public static void triggerLuredVillager(ServerPlayer player) {
        CharmAdvancements.triggerActionPerformed(player, TRIGGER_LURED_VILLAGER);
    }
}
