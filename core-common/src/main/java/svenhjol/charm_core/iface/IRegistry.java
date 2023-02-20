package svenhjol.charm_core.iface;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_api.iface.IVariantWoodMaterial;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface IRegistry {
    ResourceLocation makeId(String id);

    String getNamespace();

    ILog getLog();

    <E extends Entity> void biomeSpawn(Predicate<Holder<Biome>> predicate, MobCategory category, Supplier<EntityType<E>> entity, int weight, int minGroupSize, int maxGroupSize);

    void biomeAddition(String id, Predicate<Holder<Biome>> predicate, GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature);

    <T extends Block> Supplier<T> block(String id, Supplier<T> supplier);

    <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntity(String id, Supplier<BlockEntityType.BlockEntitySupplier<T>> builder);

    <T extends BlockEntity, U extends Block> Supplier<BlockEntityType<T>> blockEntity(String id, Supplier<BlockEntityType.BlockEntitySupplier<T>> builder, List<Supplier<U>> blocks);

    <T extends BlockEntity> void blockEntityBlocks(Supplier<BlockEntityType<T>> supplier, List<Supplier<? extends Block>> blocks);

    Supplier<BlockSetType> blockSetType(IVariantWoodMaterial material);

    <T extends Enchantment> Supplier<T> enchantment(String id, Supplier<T> enchantment);

    <T extends Entity> Supplier<EntityType<T>> entity(String id, Supplier<EntityType.Builder<T>> builder);

    <T extends LivingEntity> void entityAttributes(Supplier<EntityType<T>> entity, Supplier<AttributeSupplier.Builder> builder);

    <T extends Mob> void entitySpawnPlacement(Supplier<EntityType<T>> entity, SpawnPlacements.Type placementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> predicate);

    <T extends IFuelProvider> void fuel(Supplier<T> item);
    
    <T extends IIgniteProvider> void ignite(Supplier<T> item);

    <T extends Item> Supplier<T> item(String id, Supplier<T> supplier);

    Supplier<LootItemFunctionType> lootFunctionType(String id, Supplier<LootItemFunctionType> supplier);

    <T extends MenuType<U>, U extends AbstractContainerMenu> Supplier<T> menuType(String id, Supplier<T> supplier);

    <T extends MobEffect> Supplier<T> mobEffect(String id, Supplier<T> supplier);

    /**
     * Register a network packet and its handler.
     * On the server side, the packet would have CLIENT_TO_SERVER direction and the handler would be on the server.
     * On the client side, the packet would have SERVER_TO_CLIENT direction and the handler would be on the client.
     * @param packet The data packet. Must have @Packet annotation.
     * @param handler The handler callback.
     * @return The packet's unique ID.
     * @param <T> A concrete packet.
     * @param <U> A concrete packet handler.
     */
    <T extends IPacketRequest, U extends IPacketHandler<T>> Supplier<ResourceLocation> packet(T packet, Supplier<U> handler);

    Supplier<SimpleParticleType> particleType(String id, Supplier<SimpleParticleType> supplier);

    Supplier<PoiType> pointOfInterestType(String id, Supplier<PoiType> supplier);

    void pointOfInterestBlockStates(Supplier<ResourceKey<PoiType>> poiType, Supplier<List<BlockState>> blockStates);

    Supplier<RecipeBookType> recipeBookType(String id);

    void recipeBookTypeEnum(String name);

    <S extends RecipeSerializer<R>, R extends Recipe<?>> Supplier<S> recipeSerializer(String id, Supplier<S> serializer);

    <R extends Recipe<?>> Supplier<RecipeType<R>> recipeType(String id);

    void removeVillagerTrade(VillagerProfession profession, int tier, Predicate<ItemListing> match);

    <T extends SoundEvent> Supplier<T> soundEvent(String id, Supplier<T> supplier);

    Supplier<SoundEvent> soundEvent(String id);

    <B extends StairBlock & IIgniteProvider, I extends BlockItem> Pair<Supplier<B>, Supplier<I>> stairsBlock(String id, CharmFeature feature, IVariantMaterial material, Supplier<BlockState> state);

    <B extends Block, S extends Block> void strippable(Supplier<B> block, Supplier<S> strippedBlock);

    <I extends Item, E extends EntityType<? extends Mob>> Supplier<I> spawnEggItem(String id, Supplier<E> entity,
                                                                int primaryColor, int secondaryColor, Item.Properties properties);

    void villagerGift(String id);

    Supplier<VillagerProfession> villagerProfession(String professionId, String poitId, List<Supplier<Block>> jobSiteBlocks, Supplier<SoundEvent> workSound);

    void villagerTrade(Supplier<VillagerProfession> profession, int tier, Supplier<ItemListing> trade);

    <W extends WallSignBlock, S extends SignBlock> Supplier<W> wallSignBlock(String id, CharmFeature feature, IVariantWoodMaterial material, Supplier<S> drops, WoodType type);

    <W extends WallHangingSignBlock, S extends CeilingHangingSignBlock> Supplier<W> wallHangingSignBlock(String id, CharmFeature feature, IVariantWoodMaterial material, Supplier<S> drops, WoodType type);

    void wandererTrade(Supplier<ItemListing> supplier, boolean isRare);

    <T extends WoodType> Supplier<T> woodType(String id, IVariantWoodMaterial material);
}