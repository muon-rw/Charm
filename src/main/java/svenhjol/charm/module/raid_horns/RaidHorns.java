package svenhjol.charm.module.raid_horns;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.api.event.EntityDropItemsCallback;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.extra_wandering_trades.ExtraWanderingTrades;
import svenhjol.charm.registry.CommonRegistry;

@CommonModule(mod = Charm.MOD_ID, description = "Raid horns are sometimes dropped from raid leaders and can be used to call off or start raids.")
public class RaidHorns extends CharmModule {
    public static RaidHornItem RAID_HORN;

    public static SoundEvent CALL_PATROL_SOUND;
    public static SoundEvent CALL_OFF_RAID_SOUND;
    public static SoundEvent SQUEAK_SOUND;

    public static final ResourceLocation TRIGGER_SUMMONED_PILLAGERS = new ResourceLocation(Charm.MOD_ID, "summoned_pillagers");
    public static final ResourceLocation TRIGGER_CALLED_OFF_RAID = new ResourceLocation(Charm.MOD_ID, "called_off_raid");

    public static double lootingBoost = 0.25D;

    @Config(name = "Drop chance", description = "Chance (out of 1.0) of a patrol captain dropping a raid horn when killed by the player.")
    public static double dropChance = 0.05D;

    @Config(name = "Volume", description = "Volume of the raid horn sound effect when used.  1.0 is maximum volume.")
    public static double volume = 0.75D;

    @Override
    public void register() {
        RAID_HORN = new RaidHornItem(this);
        CALL_PATROL_SOUND = CommonRegistry.sound(new ResourceLocation(Charm.MOD_ID, "raid_horn_call_patrol"));
        CALL_OFF_RAID_SOUND = CommonRegistry.sound(new ResourceLocation(Charm.MOD_ID, "raid_horn_call_off_raid"));
        SQUEAK_SOUND = CommonRegistry.sound(new ResourceLocation(Charm.MOD_ID, "raid_horn_squeak"));
    }

    @Override
    public void runWhenEnabled() {
        EntityDropItemsCallback.AFTER.register(this::tryDrop);
        ExtraWanderingTrades.registerRareItem(RAID_HORN, 1, 30);
    }

    public InteractionResult tryDrop(LivingEntity entity, DamageSource source, int lootingLevel) {
        if (!entity.level.isClientSide
            && entity instanceof PatrollingMonster
            && source.getEntity() instanceof Player
            && entity.level.random.nextFloat() <= (dropChance + lootingBoost * lootingLevel)
        ) {
            if (((PatrollingMonster)entity).isPatrolLeader()) {
                BlockPos pos = entity.blockPosition();
                ItemStack potion = new ItemStack(RAID_HORN);
                entity.level.addFreshEntity(new ItemEntity(entity.getCommandSenderWorld(), pos.getX(), pos.getY(), pos.getZ(), potion));
            }
        }

        return InteractionResult.PASS;
    }

    public static void triggerSummoned(ServerPlayer playerEntity) {
        CharmAdvancements.ACTION_PERFORMED.trigger(playerEntity, TRIGGER_SUMMONED_PILLAGERS);
    }

    public static void triggerCalledOff(ServerPlayer playerEntity) {
        CharmAdvancements.ACTION_PERFORMED.trigger(playerEntity, TRIGGER_CALLED_OFF_RAID);
    }
}
