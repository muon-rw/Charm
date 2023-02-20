package svenhjol.charm_core.fabric.common;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
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
import svenhjol.charm_core.fabric.block.CharmStairBlock;
import svenhjol.charm_core.fabric.block.CharmWallHangingSignBlock;
import svenhjol.charm_core.fabric.block.CharmWallSignBlock;
import svenhjol.charm_core.fabric.mixin.accessor.GiveGiftToHeroAccessor;
import svenhjol.charm_core.fabric.mixin.accessor.PoiTypesAccessor;
import svenhjol.charm_core.fabric.mixin.accessor.VillagerProfessionAccessor;
import svenhjol.charm_core.helper.TextHelper;
import svenhjol.charm_core.iface.*;
import svenhjol.charm_core.mixin.accessor.AxeItemAccessor;
import svenhjol.charm_core.mixin.accessor.BlockEntityTypeAccessor;
import svenhjol.charm_core.mixin.accessor.FireBlockAccessor;
import svenhjol.charm_core.mixin.accessor.RecipeBookSettingsAccessor;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static net.minecraft.world.entity.npc.VillagerTrades.TRADES;
import static net.minecraft.world.entity.npc.VillagerTrades.WANDERING_TRADER_TRADES;


@SuppressWarnings({"unchecked", "ConstantConditions"})
public class CommonRegistry implements IRegistry {
    private final IInitializer init;
    private final Map<IPacketRequest, Supplier<IPacketHandler<IPacketRequest>>> clientPackets = new HashMap<>();
    private static final List<String> recipeBookTypeEnums = new ArrayList<>();

    public CommonRegistry(IInitializer init) {
        this.init = init;
    }

    public Map<IPacketRequest, Supplier<IPacketHandler<IPacketRequest>>> getClientPackets() {
        return clientPackets;
    }

    @Override
    public ResourceLocation makeId(String id) {
        return !id.contains(":") ? new ResourceLocation(getNamespace(), id) : new ResourceLocation(id);
    }

    @Override
    public <E extends Entity> void biomeSpawn(Predicate<Holder<Biome>> predicate, MobCategory category, Supplier<EntityType<E>> entity, int weight, int minGroupSize, int maxGroupSize) {
        Predicate<BiomeSelectionContext> biomeSelectionContext = c -> predicate.test(c.getBiomeRegistryEntry());
        BiomeModifications.addSpawn(biomeSelectionContext, category, entity.get(), weight, minGroupSize, maxGroupSize);
    }

    @Override
    public void biomeAddition(String id, Predicate<Holder<Biome>> predicate, GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature) {
        Predicate<BiomeSelectionContext> biomeSelectionContext = c -> predicate.test(c.getBiomeRegistryEntry());
        BiomeModifications.create(makeId(id + "_biome_addition")).add(
            ModificationPhase.ADDITIONS,
            biomeSelectionContext,
            ctx -> ctx.getGenerationSettings().addFeature(step, feature));
    }

    @Override
    public <T extends Block> Supplier<T> block(String id, Supplier<T> supplier) {
        getLog().debug(getClass(), "Registering block " + id);
        var registered = Registry.register(BuiltInRegistries.BLOCK, makeId(id), supplier.get());
        return () -> registered;
    }

    @Override
    public <T extends BlockEntity, U extends Block> Supplier<BlockEntityType<T>> blockEntity(String id, Supplier<BlockEntityType.BlockEntitySupplier<T>> builder, List<Supplier<U>> blocks) {
        getLog().debug(getClass(), "Registering block entity " + id);
        var registered = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, makeId(id),
            BlockEntityType.Builder.of(builder.get(), blocks.stream().map(Supplier::get).toArray(Block[]::new)).build(null));
        return () -> registered;
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntity(String id, Supplier<BlockEntityType.BlockEntitySupplier<T>> builder) {
        return blockEntity(id, builder, List.of());
    }

    @Override
    public <T extends BlockEntity> void blockEntityBlocks(Supplier<BlockEntityType<T>> supplier, List<Supplier<? extends Block>> blocks) {
        var blockEntityBlocks = ((BlockEntityTypeAccessor)supplier.get()).getValidBlocks();
        List<Block> mutable = new ArrayList<>(blockEntityBlocks);

        for (Supplier<? extends Block> blockSupplier : blocks) {
            var block = blockSupplier.get();
            if (!mutable.contains(block)) {
                mutable.add(block);
            }
        }

        ((BlockEntityTypeAccessor) supplier.get()).setValidBlocks(new HashSet<>(mutable));
    }

    @Override
    public Supplier<BlockSetType> blockSetType(IVariantWoodMaterial material) {
        var registered = BlockSetType.register(new BlockSetType(material.getSerializedName()));
        return () -> registered;
    }

    @Override
    public <T extends Enchantment> Supplier<T> enchantment(String id, Supplier<T> enchantment) {
        getLog().debug(getClass(), "Registering enchantment " + id);
        var registered = Registry.register(BuiltInRegistries.ENCHANTMENT, makeId(id), enchantment.get());
        return () -> registered;
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> entity(String id, Supplier<EntityType.Builder<T>> builder) {
        getLog().debug(getClass(), "Registering entity " + id);
        var registered = Registry.register(BuiltInRegistries.ENTITY_TYPE, makeId(id), builder.get().build(makeId(id).toString()));
        return () -> registered;
    }

    @Override
    public <T extends LivingEntity> void entityAttributes(Supplier<EntityType<T>> entity, Supplier<AttributeSupplier.Builder> builder) {
        FabricDefaultAttributeRegistry.register(entity.get(), builder.get());
    }

    @Override
    public <T extends Mob> void entitySpawnPlacement(Supplier<EntityType<T>> entity, SpawnPlacements.Type placementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> predicate) {
        SpawnPlacements.register(entity.get(), placementType, heightmapType, predicate);
    }

    @Override
    public <T extends IFuelProvider> void fuel(Supplier<T> provider) {
        var item = provider.get();
        FuelRegistry.INSTANCE.add((ItemLike) item, item.fuelTime());
    }

    @Override
    public <T extends IIgniteProvider> void ignite(Supplier<T> provider) {
        var block = provider.get();
        ((FireBlockAccessor)Blocks.FIRE).invokeSetFlammable((Block)block, block.igniteChance(), block.burnChance());
    }

    @Override
    public <T extends Item> Supplier<T> item(String id, Supplier<T> supplier) {
        getLog().debug(getClass(), "Registering item " + id);
        var registered = Registry.register(BuiltInRegistries.ITEM, makeId(id), supplier.get());
        return () -> registered;
    }

    @Override
    public Supplier<LootItemFunctionType> lootFunctionType(String id, Supplier<LootItemFunctionType> supplier) {
        getLog().debug(getClass(), "Registering loot function type " + id);
        var registered = Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, makeId(id), supplier.get());
        return () -> registered;
    }

    @Override
    public <T extends MenuType<U>, U extends AbstractContainerMenu> Supplier<T> menuType(String id, Supplier<T> supplier) {
        getLog().debug(getClass(), "Registering menu type " + id);
        var registered = Registry.register(BuiltInRegistries.MENU, makeId(id), supplier.get());
        return () -> registered;
    }

    @Override
    public <T extends MobEffect> Supplier<T> mobEffect(String id, Supplier<T> supplier) {
        getLog().debug(getClass(), "Registering mob effect " + id);
        var registered = Registry.register(BuiltInRegistries.MOB_EFFECT, makeId(id), supplier.get());
        return () -> registered;
    }

    @Override
    public <T extends IPacketRequest, U extends IPacketHandler<T>> Supplier<ResourceLocation> packet(T packet, Supplier<U> handler) {
        var id = packet.id();
        getLog().debug(getClass(), "Registering packet " + id);

        switch (packet.direction()) {
            case CLIENT_TO_SERVER -> ServerPlayNetworking.registerGlobalReceiver(id, (server, player, listener, buf, sender) -> {
                packet.decode(buf);
                server.execute(() -> handler.get().handle(packet, player));
            });
            // We can't import ClientPlayNetworking here, so defer the client packet registrations.
            case SERVER_TO_CLIENT -> clientPackets.put(packet, (Supplier<IPacketHandler<IPacketRequest>>)handler);
        }

        return () -> id;
    }

    @Override
    public Supplier<SimpleParticleType> particleType(String id, Supplier<SimpleParticleType> supplier) {
        getLog().debug(getClass(), "Registering particle type " + id);
        var registered = Registry.register(BuiltInRegistries.PARTICLE_TYPE, makeId(id), supplier.get());
        return () -> registered;
    }

    @Override
    public Supplier<PoiType> pointOfInterestType(String id, Supplier<PoiType> supplier) {
        var poiType = supplier.get();
        var poitKey = ResourceKey.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE.key(), makeId(id));
        Registry.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE, poitKey, poiType);
        PoiTypesAccessor.invokeRegisterBlockStates(BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(poitKey), Set.of());
        return () -> poiType;
    }

    @Override
    public void pointOfInterestBlockStates(Supplier<ResourceKey<PoiType>> poiType, Supplier<List<BlockState>> blockStates) {
        var holder = BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(poiType.get());

        var matchingStates = new ArrayList<>(holder.value().matchingStates());
        matchingStates.addAll(blockStates.get());

        matchingStates.forEach(state -> PoiTypesAccessor.getTypeByState().put(state, holder));
    }

    @Override
    public Supplier<RecipeBookType> recipeBookType(String id) {
        var upper = id.toUpperCase(Locale.ROOT);
        var capitalized = TextHelper.capitalize(id.toLowerCase(Locale.ROOT));

        RecipeBookType type = RecipeBookType.valueOf(upper);
        var tagFields = new HashMap<>(RecipeBookSettingsAccessor.getTagFields());
        tagFields.put(type, Pair.of("is" + capitalized + "GuiOpen", "is" + capitalized + "FilteringCraftable"));
        RecipeBookSettingsAccessor.setTagFields(tagFields);

        return () -> type;
    }

    @Override
    public void recipeBookTypeEnum(String name) {
        recipeBookTypeEnums.add(name);
    }

    @Override
    public <S extends RecipeSerializer<T>, T extends Recipe<?>> Supplier<S> recipeSerializer(String id, Supplier<S> serializer) {
        getLog().debug(getClass(), "Registering recipe serializer " + id);
        var registered = RecipeSerializer.register(makeId(id).toString(), serializer.get());
        return () -> registered;
    }

    @Override
    public <R extends Recipe<?>> Supplier<RecipeType<R>> recipeType(String id) {
        getLog().debug(getClass(), "Registering recipe type " + id);
        var registered = RecipeType.register(makeId(id).toString());
        return () -> (RecipeType<R>) registered;
    }

    @Override
    public <T extends SoundEvent> Supplier<T> soundEvent(String id, Supplier<T> supplier) {
        getLog().debug(getClass(), "Registering sound event " + id);
        var registered = Registry.register(BuiltInRegistries.SOUND_EVENT, makeId(id), supplier.get());
        return () -> registered;
    }

    @Override
    public Supplier<SoundEvent> soundEvent(String id) {
        getLog().debug(getClass(), "Registering sound event " + id);
        if (id.contains(":")) {
            var res = new ResourceLocation(id);
            return soundEvent(res.getPath(), () -> SoundEvent.createVariableRangeEvent(res));
        } else {
            return soundEvent(id, () -> SoundEvent.createVariableRangeEvent(makeId(id)));
        }
    }

    @Override
    public <B extends StairBlock & IIgniteProvider, I extends BlockItem> Pair<Supplier<B>, Supplier<I>> stairsBlock(String id, CharmFeature feature, IVariantMaterial material, Supplier<BlockState> state) {
        getLog().debug(getClass(), "Registering stairs block " + id);
        var block = block(id, () -> new CharmStairBlock(feature, material, state.get()));
        var item = item(id, () -> new CharmStairBlock.BlockItem(feature, block));
        return Pair.of((Supplier<B>)block, (Supplier<I>)item);
    }

    @Override
    public <B extends Block, S extends Block> void strippable(Supplier<B> block, Supplier<S> strippedBlock) {
        // Make axe strippables map mutable.
        var strippables = AxeItemAccessor.getStrippables();
        AxeItemAccessor.setStrippables(new HashMap<>(strippables));
        AxeItemAccessor.getStrippables().put(block.get(), strippedBlock.get());
    }

    @Override
    public <I extends Item, E extends EntityType<? extends Mob>> Supplier<I> spawnEggItem(String id, Supplier<E> entity, int primaryColor, int secondaryColor, Item.Properties properties) {
        return (Supplier<I>) item(id, () -> new SpawnEggItem(entity.get(), primaryColor, secondaryColor, properties));
    }

    @Override
    public void villagerGift(String id) {
        var res = makeId(id);
        var profession = BuiltInRegistries.VILLAGER_PROFESSION.getOptional(res).orElseThrow();
        var lootTable = new ResourceLocation(res.getNamespace(), "gameplay/hero_of_the_village/" + res.getPath() + "_gift");
        GiveGiftToHeroAccessor.getGifts().put(profession, lootTable);
    }

    @Override
    public Supplier<VillagerProfession> villagerProfession(String professionId, String poitId, List<Supplier<Block>> jobSiteBlocks, Supplier<SoundEvent> workSound) {
        var poitKey = ResourceKey.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE.key(), makeId(poitId));
        var registered = VillagerProfessionAccessor.invokeRegister(
            makeId(professionId).toString(),
            poitKey,
            ImmutableSet.of(),
            jobSiteBlocks.stream().map(Supplier::get).collect(ImmutableSet.toImmutableSet()),
            workSound.get()
        );
        TRADES.put(registered, new Int2ObjectOpenHashMap<>());
        return () -> registered;
    }

    @Override
    public void villagerTrade(Supplier<VillagerProfession> supplier, int tier, Supplier<VillagerTrades.ItemListing> trade) {
        var profession = supplier.get();
        var trades = getMutableTrades(profession);
        trades.get(tier).add(trade.get());
        reassembleTrades(profession, trades);
    }

    @Override
    public <W extends WallSignBlock, S extends SignBlock> Supplier<W> wallSignBlock(String id, CharmFeature feature, IVariantWoodMaterial material, Supplier<S> drops, WoodType type) {
        getLog().debug(getClass(), "Registering wall sign block " + id);
        var block = block(id, () -> new CharmWallSignBlock(feature, material, drops.get(), type));
        return (Supplier<W>)block;
    }

    @Override
    public <W extends WallHangingSignBlock, S extends CeilingHangingSignBlock> Supplier<W> wallHangingSignBlock(String id, CharmFeature feature, IVariantWoodMaterial material, Supplier<S> drops, WoodType type) {
        getLog().debug(getClass(), "Registering wall hanging sign block " + id);
        var block = block(id, () -> new CharmWallHangingSignBlock(feature, material, drops.get(), type));
        return (Supplier<W>)block;
    }

    @Override
    public void removeVillagerTrade(VillagerProfession profession, int level, Predicate<VillagerTrades.ItemListing> match) {
        var trades = getMutableTrades(profession);
        trades.get(level).stream()
            .filter(match)
            .findFirst()
            .ifPresent(trade -> {
                getLog().debug(getClass(), "Removing trade for profession " + profession.name() + ", level: " + level + ", trade: " + trade);
                trades.get(level).remove(trade);
                reassembleTrades(profession, trades);
            });
    }

    @Override
    public void wandererTrade(Supplier<VillagerTrades.ItemListing> supplier, boolean isRare) {
        List<VillagerTrades.ItemListing> trades = NonNullList.create();
        int index = isRare ? 2 : 1;

        trades.addAll(Arrays.asList(WANDERING_TRADER_TRADES.get(index)));
        trades.add(supplier.get());

        WANDERING_TRADER_TRADES.put(index, trades.toArray(new VillagerTrades.ItemListing[0]));
    }

    @Override
    public <T extends WoodType> Supplier<T> woodType(String id, IVariantWoodMaterial material) {
        var registered = WoodType.register(new WoodType(makeId(id).toString().replace(":", "_"), material.getBlockSetType()));
        return () -> (T)registered;
    }

    @Override
    public String getNamespace() {
        return init.getRegistryNamespace();
    }

    @Override
    public ILog getLog() {
        return init.getLog();
    }

    public static List<String> getRecipeBookTypeEnums() {
        return recipeBookTypeEnums;
    }

    protected Int2ObjectMap<List<VillagerTrades.ItemListing>> getMutableTrades(VillagerProfession profession) {
        var fixedTrades = TRADES.get(profession);
        Int2ObjectMap<List<VillagerTrades.ItemListing>> mutableTrades = new Int2ObjectOpenHashMap<>();

        for (int i = 1; i <= 5; i++) {
            mutableTrades.put(i, NonNullList.create());
        }

        fixedTrades.int2ObjectEntrySet().forEach(e
            -> Arrays.stream(e.getValue())
            .forEach(a -> mutableTrades.get(e.getIntKey()).add(a)));

        return mutableTrades;
    }

    protected void reassembleTrades(VillagerProfession profession, Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
        Int2ObjectMap<VillagerTrades.ItemListing[]> mappedTrades = new Int2ObjectOpenHashMap<>();
        trades.int2ObjectEntrySet().forEach(e
            -> mappedTrades.put(e.getIntKey(), e.getValue().toArray(new VillagerTrades.ItemListing[0])));

        TRADES.put(profession, mappedTrades);
    }
}