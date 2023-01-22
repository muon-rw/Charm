package svenhjol.charm.feature.coral_squids;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import svenhjol.charm.Charm;
import svenhjol.charm_api.iface.IProvidesWandererTrades;
import svenhjol.charm_api.iface.IWandererTrade;
import svenhjol.charm_core.annotation.Configurable;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Coral Squids spawn near coral in warm oceans.")
public class CoralSquids extends CharmFeature implements IProvidesWandererTrades {
    private static final String ID = "coral_squid";
    public static Supplier<Item> SPAWN_EGG_ITEM;
    public static Supplier<Item> BUCKET_ITEM;
    public static Supplier<EntityType<CoralSquidEntity>> ENTITY;
    public static TagKey<Biome> SPAWNS_CORAL_SQUIDS = TagKey.create(Registries.BIOME, Charm.makeId("spawns_coral_squids"));

    @Configurable(name = "Drop chance", description = "Chance (out of 1.0) of a coral squid dropping coral when killed.")
    public static double dropChance = 0.2D;

    @Override
    public void register() {
        ENTITY = Charm.REGISTRY.entity(ID, () -> EntityType.Builder
            .of(CoralSquidEntity::new, MobCategory.WATER_AMBIENT)
            .sized(0.54F, 0.54F));

        SPAWN_EGG_ITEM = Charm.REGISTRY.spawnEggItem("coral_squid_spawn_egg", ENTITY, 0x0000FF, 0xFF00FF, new Item.Properties());
        BUCKET_ITEM = Charm.REGISTRY.item("coral_squid_bucket", () -> new CoralSquidBucketItem(this));

        Charm.REGISTRY.biomeSpawn(holder -> holder.is(SPAWNS_CORAL_SQUIDS), MobCategory.WATER_AMBIENT, ENTITY, 50, 2, 4);
        Charm.REGISTRY.entitySpawnPlacement(ENTITY, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CoralSquidEntity::canSpawn);
        Charm.REGISTRY.entityAttributes(ENTITY, CoralSquidEntity::createSquidAttributes);
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
                return BUCKET_ITEM.get();
            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public int getCost() {
                return 12;
            }
        });
    }
}