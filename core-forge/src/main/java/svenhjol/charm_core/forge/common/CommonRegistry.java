package svenhjol.charm_core.forge.common;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.forge.block.CharmStairBlock;
import svenhjol.charm_core.forge.block.CharmWallSignBlock;
import svenhjol.charm_core.forge.network.ClientToServerPacket;
import svenhjol.charm_core.forge.network.ServerToClientPacket;
import svenhjol.charm_core.forge.registry.*;
import svenhjol.charm_core.forge.server.ServerNetwork;
import svenhjol.charm_core.helper.TextHelper;
import svenhjol.charm_core.iface.*;
import svenhjol.charm_core.mixin.accessor.RecipeBookSettingsAccessor;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static net.minecraft.world.entity.npc.VillagerTrades.TRADES;

@SuppressWarnings({"unchecked", "rawtypes", "ConstantConditions"})
public class CommonRegistry implements IRegistry {
    private final IInitializer init;
    private final CommonDeferred deferred;
    private final DeferredRegister<Block> blocks;
    private final DeferredRegister<BlockEntityType<?>> blockEntityTypes;
    private final DeferredRegister<Enchantment> enchantments;
    private final DeferredRegister<EntityType<?>> entityTypes;
    private final DeferredRegister<Item> items;
    private final DeferredRegister<MenuType<?>> menuTypes;
    private final DeferredRegister<MobEffect> mobEffects;
    private final DeferredRegister<ParticleType<?>> particleTypes;
    private final DeferredRegister<PoiType> poiTypes;
    private final DeferredRegister<RecipeSerializer<?>> recipeSerializers;
    private final DeferredRegister<RecipeType<?>> recipeTypes;
    private final DeferredRegister<SoundEvent> soundEvents;
    private final DeferredRegister<VillagerProfession> villagerProfessions;

    public CommonRegistry(IInitializer init) {
        this.init = init;
        this.deferred = new CommonDeferred();

        blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, getNamespace());
        blockEntityTypes = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, getNamespace());
        enchantments = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, getNamespace());
        entityTypes = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, getNamespace());
        items = DeferredRegister.create(ForgeRegistries.ITEMS, getNamespace());
        menuTypes = DeferredRegister.create(ForgeRegistries.MENU_TYPES, getNamespace());
        mobEffects = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, getNamespace());
        particleTypes = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, getNamespace());
        poiTypes = DeferredRegister.create(ForgeRegistries.POI_TYPES, getNamespace());
        recipeSerializers = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, getNamespace());
        recipeTypes = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, getNamespace());
        soundEvents = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, getNamespace());
        villagerProfessions = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, getNamespace());
    }

    private int messageId = 1;

    public CommonDeferred getDeferred() {
        return deferred;
    }

    @Override
    public ResourceLocation makeId(String id) {
        return !id.contains(":") ? new ResourceLocation(getNamespace(), id) : new ResourceLocation(id);
    }

    /**
     * This is handled via datapacks in Forge.
     * <a href="https://forge.gemwire.uk/wiki/Biome_Modifiers#Biome_Modifier_JSONs">Forge's Biome Modifier JSON</a>
     */
    @Override
    public <E extends Entity> void biomeSpawn(Predicate<Holder<Biome>> predicate, MobCategory category, Supplier<EntityType<E>> entity, int weight, int minGroupSize, int maxGroupSize) {
        // no op. Handle spawns via data/modid/forge/biome_modifier.
    }

    /**
     * This is handled via datapacks in Forge.
     * <a href="https://forge.gemwire.uk/wiki/Biome_Modifiers#Add_Features">Forge's Biome Feature Additions</a>
     */
    @Override
    public void biomeAddition(String id, Predicate<Holder<Biome>> predicate, GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature) {
        // no op. Handle additions via data/modid/forge/biome_modifier.
    }

    public <T extends Block> RegistryObject<T> block(String id, Supplier<T> supplier) {
        getLog().debug(getClass(), "Registering block " + id);
        return blocks.register(id, supplier);
    }

    @Override
    public <T extends BlockEntity> RegistryObject<BlockEntityType<T>> blockEntity(String id, Supplier<BlockEntityType.BlockEntitySupplier<T>> builder) {
        return blockEntity(id, builder, List.of());
    }

    @Override
    public <T extends BlockEntity, U extends Block> RegistryObject<BlockEntityType<T>> blockEntity(String id, Supplier<BlockEntityType.BlockEntitySupplier<T>> builder, List<Supplier<U>> blocks) {
        getLog().debug(getClass(), "Registering block entity " + id);
        return blockEntityTypes.register(id, () -> BlockEntityType.Builder.of(builder.get(), blocks.stream().map(Supplier::get).toArray(Block[]::new)).build(null));
    }

    @Override
    public <T extends BlockEntity> void blockEntityBlocks(Supplier<BlockEntityType<T>> supplier, List<Supplier<? extends Block>> blocks) {
        deferred.blockEntityBlocks.add(new DeferredBlockEntityBlocks(supplier, blocks));
    }

    @Override
    public <T extends Enchantment> Supplier<T> enchantment(String id, Supplier<T> enchantment) {
        getLog().debug(getClass(), "Registering enchantment " + id);
        return enchantments.register(id, enchantment);
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> entity(String id, Supplier<EntityType.Builder<T>> builder) {
        getLog().debug(getClass(), "Registering entity " + id);
        return entityTypes.register(id, () -> builder.get().build(id));
    }

    @Override
    public <T extends LivingEntity> void entityAttributes(Supplier<EntityType<T>> entity, Supplier<AttributeSupplier.Builder> builder) {
        deferred.entityAttributes.add(new DeferredEntityAttributes(entity, builder));
    }

    @Override
    public <T extends Mob> void entitySpawnPlacement(Supplier<EntityType<T>> entity, SpawnPlacements.Type placementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> predicate) {
        deferred.entitySpawnPlacements.add(new DeferredEntitySpawnPlacement(entity, placementType, heightmapType, predicate));
    }

    @Override
    public <T extends IFuelProvider> void fuel(Supplier<T> provider) {
        deferred.itemFuels.add(new DeferredItemFuel(provider));
    }

    @Override
    public <T extends IIgniteProvider> void ignite(Supplier<T> provider) {
        deferred.blockIgnites.add(new DeferredBlockIgnite(provider));
    }

    @Override
    public <T extends Item> RegistryObject<T> item(String id, Supplier<T> supplier) {
        getLog().debug(getClass(), "Registering item " + id);
        return items.register(id, supplier);
    }

    @Override
    public Supplier<LootItemFunctionType> lootFunctionType(String id, Supplier<LootItemFunctionType> supplier) {
        getLog().debug(getClass(), "Registering loot function type " + id);
        deferred.lootFunctionTypes.add(new DeferredLootFunctionType(makeId(id), supplier));
        return supplier;
    }

    @Override
    public <T extends MenuType<U>, U extends AbstractContainerMenu> RegistryObject<T> menuType(String id, Supplier<T> supplier) {
        getLog().debug(getClass(), "Registering menu type " + id);
        return menuTypes.register(id, supplier);
    }

    @Override
    public <T extends MobEffect> Supplier<T> mobEffect(String id, Supplier<T> supplier) {
        getLog().debug(getClass(), "Registering mob effect " + id);
        return mobEffects.register(id, supplier);
    }

    @Override
    public <T extends IPacketRequest, U extends IPacketHandler<T>> Supplier<ResourceLocation> packet(T packet, Supplier<U> handler) {
        var id = packet.id();
        getLog().debug(getClass(), "Registering packet " + id);

        switch (packet.direction()) {
            case CLIENT_TO_SERVER -> {
                var wrappedPacket = new ClientToServerPacket<>(packet, handler);
                ServerNetwork.INSTANCE.registerMessage(++messageId, (Class)packet.getClass(), wrappedPacket::encode, wrappedPacket::decode, wrappedPacket::handle);
            }
            case SERVER_TO_CLIENT -> {
                var wrappedPacket = new ServerToClientPacket<>(packet, handler);
                ServerNetwork.INSTANCE.registerMessage(++messageId, (Class)packet.getClass(), wrappedPacket::encode, wrappedPacket::decode, wrappedPacket::handle);
            }
        }

        return () -> id;
    }

    @Override
    public Supplier<SimpleParticleType> particleType(String id, Supplier<SimpleParticleType> supplier) {
        getLog().debug(getClass(), "Registering particle type " + id);
        return particleTypes.register(id, supplier);
    }

    @Override
    public RegistryObject<PoiType> pointOfInterestType(String id, Supplier<PoiType> supplier) {
        getLog().debug(getClass(), "Registering point of interest type " + id);
        return poiTypes.register(id, supplier);
    }

    @Override
    public void pointOfInterestBlockStates(Supplier<ResourceKey<PoiType>> poiType, Supplier<List<BlockState>> blockStates) {
        deferred.pointOfInterestBlockStates.add(new DeferredPointOfInterestBlockStates(poiType, blockStates));
    }

    @Override
    public Supplier<RecipeBookType> recipeBookType(String id) {
        var upper = id.toUpperCase(Locale.ROOT);
        var capitalized = TextHelper.capitalize(id.toLowerCase(Locale.ROOT));

        RecipeBookType type = RecipeBookType.valueOf(upper);
        var tagFields = RecipeBookSettingsAccessor.getTagFields();
        tagFields.put(type, Pair.of("is" + capitalized + "GuiOpen", "is" + capitalized + "FilteringCraftable"));
        RecipeBookSettingsAccessor.setTagFields(tagFields);

        return () -> type;
    }

    @Override
    public void recipeBookTypeEnum(String name) {
        RecipeBookType.create(name.toUpperCase(Locale.ROOT));
    }

    @Override
    public <S extends RecipeSerializer<R>, R extends Recipe<?>> RegistryObject<S> recipeSerializer(String id, Supplier<S> serializer) {
        getLog().debug(getClass(), "Registering recipe serializer " + id);
        return recipeSerializers.register(id, serializer);
    }

    @Override
    public <R extends Recipe<?>> Supplier<RecipeType<R>> recipeType(String id) {
        getLog().debug(getClass(), "Registering recipe type " + id);
        var type = new RecipeType<R>() {
            @Override
            public String toString() {
                return id;
            }
        };

        return recipeTypes.register(id, () -> type);
    }

    @Override
    public void removeVillagerTrade(VillagerProfession profession, int tier, Predicate<VillagerTrades.ItemListing> match) {
        var trades = getMutableTrades(profession);
        trades.get(tier).stream()
            .filter(match)
            .findFirst()
            .ifPresent(trade -> {
                getLog().debug(getClass(), "Removing trade for profession " + profession.name() + ", level: " + tier + ", trade: " + trade);
                trades.get(tier).remove(trade);
                reassembleTrades(profession, trades);
            });
    }

    @Override
    public <T extends SoundEvent> Supplier<T> soundEvent(String id, Supplier<T> supplier) {
        getLog().debug(getClass(), "Registering sound event " + id);
        return soundEvents.register(id, supplier);
    }

    @Override
    public Supplier<SoundEvent> soundEvent(String id) {
        if (id.contains(":")) {
            var res = new ResourceLocation(id);
            return soundEvent(res.getPath(), () -> new SoundEvent(res, 16, false)); // TODO: check 16 false
        } else {
            return soundEvent(id, () -> new SoundEvent(makeId(id), 16, false)); // TODO: check 16 false
        }
    }

    @Override
    public <B extends StairBlock & IIgniteProvider, I extends BlockItem> Pair<Supplier<B>, Supplier<I>> stairsBlock(String id, CharmFeature feature, IVariantMaterial material, Supplier<BlockState> state) {
        getLog().debug(getClass(), "Registering stairs block " + id);
        var block = block(id, () -> new CharmStairBlock(feature, material, state));
        var item = item(id, () -> new CharmStairBlock.BlockItem(feature, block));
        return Pair.of((Supplier<B>)block, (Supplier<I>)item);
    }

    @Override
    public <B extends Block, S extends Block> void strippable(Supplier<B> block, Supplier<S> strippedBlock) {
        deferred.strippables.add(new DeferredStrippable(block, strippedBlock));
    }

    @Override
    public <I extends Item, E extends EntityType<? extends Mob>> RegistryObject<I> spawnEggItem(String id, Supplier<E> entity, int primaryColor, int secondaryColor, Item.Properties properties) {
        return (RegistryObject<I>) items.register(id, () -> new ForgeSpawnEggItem(entity, primaryColor, secondaryColor, properties));
    }

    @Override
    public void villagerGift(String id) {
        getLog().debug(getClass(), "Registering villager gift " + id);
        deferred.villagerGifts.add(new DeferredVillagerGift(makeId(id)));
    }

    @Override
    public Supplier<VillagerProfession> villagerProfession(String professionId, String poitId, List<Supplier<Block>> jobSiteBlocks, Supplier<SoundEvent> workSound) {
        getLog().debug(getClass(), "Registering villager profession " + professionId);
        Supplier<VillagerProfession> profession = () -> {
            var res = makeId(professionId);
            var poitKey = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, makeId(poitId));
            return new VillagerProfession(
                res.toString(),
                h -> h.is(poitKey),
                h -> h.is(poitKey),
                ImmutableSet.of(),
                jobSiteBlocks.stream().map(Supplier::get).collect(ImmutableSet.toImmutableSet()),
                workSound.get());
        };
        return villagerProfessions.register(professionId, profession);
    }

    @Override
    public void villagerTrade(Supplier<VillagerProfession> profession, int tier, Supplier<VillagerTrades.ItemListing> trade) {
        deferred.villagerTrades.add(new DeferredVillagerTrade(profession, trade, tier));
    }

    @Override
    public <W extends WallSignBlock, S extends SignBlock> Supplier<W> wallSignBlock(String id, CharmFeature feature, IVariantMaterial material, Supplier<S> drops, WoodType type) {
        getLog().debug(getClass(), "Registering wall sign block " + id);
        var block = block(id, () -> new CharmWallSignBlock(feature, material, drops, type));
        return (Supplier<W>)block;
    }

    @Override
    public void wandererTrade(Supplier<VillagerTrades.ItemListing> supplier, boolean isRare) {
        deferred.wandererTrades.add(new DeferredWandererTrade(supplier, isRare));
    }

    @Override
    public <T extends WoodType> Supplier<T> woodType(String id) {
        getLog().debug(getClass(), "Registering wood type " + id);
        var registered = WoodType.register(new WoodType(makeId(id).toString().replace(":", "_")));
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

    public void register(IEventBus modEventBus) {
        blocks.register(modEventBus);
        blockEntityTypes.register(modEventBus);
        enchantments.register(modEventBus);
        entityTypes.register(modEventBus);
        items.register(modEventBus);
        menuTypes.register(modEventBus);
        mobEffects.register(modEventBus);
        particleTypes.register(modEventBus);
        poiTypes.register(modEventBus);
        recipeSerializers.register(modEventBus);
        recipeTypes.register(modEventBus);
        soundEvents.register(modEventBus);
        villagerProfessions.register(modEventBus);
    }
}