package svenhjol.charm.feature.endermite_powder;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.levelgen.structure.Structure;
import svenhjol.charm.Charm;
import svenhjol.charm_api.event.EntityKilledDropEvent;
import svenhjol.charm_api.iface.IProvidesWandererTrades;
import svenhjol.charm_api.iface.IWandererTrade;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.init.CharmApi;

import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Endermites drop endermite powder that can be used to locate an End City.")
public class EndermitePowder extends CharmFeature implements IProvidesWandererTrades {
    static final String ID = "endermite_powder";
    static Supplier<EntityType<EndermitePowderEntity>> ENTITY;
    static Supplier<EndermitePowderItem> ITEM;
    static Supplier<SoundEvent> LAUNCH_SOUND;
    public static TagKey<Structure> ENDERMITE_POWDER_LOCATED;

    @Override
    public void register() {
        ITEM = Charm.REGISTRY.item(ID, () -> new EndermitePowderItem(this));
        LAUNCH_SOUND = Charm.REGISTRY.soundEvent("endermite_powder_launch");

        ENDERMITE_POWDER_LOCATED = TagKey.create(Registries.STRUCTURE,
            Charm.makeId("endermite_powder_located"));

        ENTITY = Charm.REGISTRY.entity(ID, () -> EntityType.Builder
            .<EndermitePowderEntity>of(EndermitePowderEntity::new, MobCategory.MISC)
            .clientTrackingRange(80)
            .updateInterval(10)
            .sized(2.0F, 2.0F));

        CharmApi.registerProvider(this);
    }

    @Override
    public void runWhenEnabled() {
        EntityKilledDropEvent.INSTANCE.handle(this::handleEntityKilledDrop);
    }

    private InteractionResult handleEntityKilledDrop(LivingEntity entity, DamageSource source, int lootingLevel) {
        if (!entity.level.isClientSide() && entity instanceof Endermite) {
            var random = entity.getRandom();
            var level = entity.getCommandSenderWorld();
            var pos = entity.blockPosition();
            var amount = random.nextInt(2) + random.nextInt(lootingLevel + 1);
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(),
                new ItemStack(ITEM.get(), amount)));
        }
        return InteractionResult.PASS;
    }

    @Override
    public List<IWandererTrade> getWandererTrades() {
        return List.of();
    }

    @Override
    public List<IWandererTrade> getRareWandererTrades() {
        return List.of(new IWandererTrade() {
            @Override
            public ItemLike getItem() {
                return ITEM.get();
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public int getCost() {
                return 20;
            }
        });
    }
}
