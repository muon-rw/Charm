package svenhjol.charm.feature.mooblooms;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import svenhjol.charm.Charm;
import svenhjol.charm_api.event.EntityJoinEvent;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Mooblooms are cow-like mobs that come in a variety of flower types.\n" +
    "They spawn flowers where they walk and can be milked for suspicious stew.")
public class Mooblooms extends CharmFeature {
    static final String ID = "moobloom";
    static Supplier<Item> SPAWN_EGG_ITEM;
    static Supplier<EntityType<MoobloomEntity>> ENTITY;
    public static final TagKey<Biome> SPAWNS_MOOBLOOMS = TagKey.create(Registries.BIOME, Charm.makeId("spawns_mooblooms"));
    public static final TagKey<Biome> SPAWNS_CHERRY_BLOSSOM_MOOBLOOMS = TagKey.create(Registries.BIOME, Charm.makeId("spawns_cherry_blossom_mooblooms"));


    @Override
    public void register() {
        ENTITY = Charm.REGISTRY.entity(ID, () -> EntityType.Builder
            .of(MoobloomEntity::new, MobCategory.CREATURE)
            .sized(0.9F, 1.4F)
            .clientTrackingRange(10));

        SPAWN_EGG_ITEM = Charm.REGISTRY.spawnEggItem("moobloom_spawn_egg", ENTITY, 0xFFFF00, 0xFFFFFF, new Item.Properties());

        Charm.REGISTRY.biomeSpawn(holder -> holder.is(SPAWNS_MOOBLOOMS), MobCategory.CREATURE, ENTITY, 30, 2, 4);
        Charm.REGISTRY.entitySpawnPlacement(ENTITY, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MoobloomEntity::canSpawn);
        Charm.REGISTRY.entityAttributes(ENTITY, MoobloomEntity::createAttributes);
    }

    @Override
    public void runWhenEnabled() {
        EntityJoinEvent.INSTANCE.handle(this::handleEntityJoin);
    }

    private void handleEntityJoin(Entity entity, Level level) {
        if (entity instanceof Bee bee) {
            var hasGoal = bee.getGoalSelector().getAvailableGoals().stream().anyMatch(
                goal -> goal.getGoal() instanceof BeeMoveToMoobloomGoal);

            if (!hasGoal) {
                bee.getGoalSelector().addGoal(4, new BeeMoveToMoobloomGoal(bee));
            }
        }
    }
}
