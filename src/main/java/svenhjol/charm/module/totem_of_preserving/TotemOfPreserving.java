package svenhjol.charm.module.totem_of_preserving;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.api.event.EntityDropXpCallback;
import svenhjol.charm.api.event.PlayerDropInventoryCallback;
import svenhjol.charm.api.event.TotemOfPreservingEvents;
import svenhjol.charm.helper.ItemHelper;
import svenhjol.charm.helper.LogHelper;
import svenhjol.charm.helper.TextHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.totem_works_from_inventory.TotemWorksFromInventory;
import svenhjol.charm.registry.CommonRegistry;

import java.util.*;

@CommonModule(mod = Charm.MOD_ID, description = """
    The player's inventory items will be preserved in the Totem of Preserving upon death.
    By default, a new totem will always be spawned to preserve items upon dying ('Grave mode').
    If Grave mode is not enabled, you must be holding a totem in order for it to preserve your items.""")
public class TotemOfPreserving extends CharmModule {
    public static TotemOfPreservingItem ITEM;
    public static LootItemFunctionType CHEST_LOOT_FUNCTION;
    public static LootItemFunctionType MOB_LOOT_FUNCTION;
    public static final ResourceLocation CHEST_LOOT_ID = new ResourceLocation(Charm.MOD_ID, "totem_of_preserving_chest_loot");
    public static final ResourceLocation MOB_LOOT_ID = new ResourceLocation(Charm.MOD_ID, "totem_of_preserving_mob_loot");
    public static final ResourceLocation TRIGGER_USED_TOTEM_OF_PRESERVING = new ResourceLocation(Charm.MOD_ID, "used_totem_of_preserving");
    public static final List<ResourceLocation> VALID_MOB_LOOT = new ArrayList<>();
    public static final List<ResourceLocation> VALID_CHEST_LOOT = new ArrayList<>();
    public static final ResourceLocation BLOCK_ID = new ResourceLocation(Charm.MOD_ID, "totem_block");
    public static TotemBlock BLOCK;
    public static BlockEntityType<TotemBlockEntity> BLOCK_ENTITY;

    public static Map<ResourceLocation, List<BlockPos>> PROTECT_POSITIONS = new HashMap<>();

    @Config(name = "Grave mode", description = "If true, a new totem will be spawned to preserve items upon dying.\n" +
        "Players do NOT need to be holding a Totem of Preserving.")
    public static boolean graveMode = true;

    @Config(name = "Owner only", description = "If true, only the owner of the totem may pick it up.")
    public static boolean ownerOnly = false;

    @Config(name = "Mobs drop totems", description = "Mobs that have a chance to drop a totem of preserving.\n" +
        "This does not apply if Grave mode is true.")
    public static List<String> configMobDrops = Arrays.asList(
        "entities/witch", "entities/pillager"
    );

    @Config(name = "Chests contain totems", description = "Chest loot tables that will always contain a totem of preserving.\n" +
        "This does not apply if Grave mode is true.")
    public static List<String> configChestLoot = Arrays.asList(
        "chests/pillager_outpost", "chests/woodland_mansion"
    );

    @Config(name = "Preserve XP", description = "If true, the totem will preserve the player's experience and restore when broken.")
    public static boolean preserveXp = false;

    @Config(name = "Show death position", description = "If true, the coordinates where you died will be added to the player's chat screen.")
    public static boolean showDeathPosition = false;

    @Override
    public void register() {
        ITEM = new TotemOfPreservingItem(this);
        BLOCK = new TotemBlock(this);
        BLOCK_ENTITY = CommonRegistry.blockEntity(BLOCK_ID, TotemBlockEntity::new, BLOCK);

        configMobDrops.stream().map(ResourceLocation::new).forEach(VALID_MOB_LOOT::add);
        configChestLoot.stream().map(ResourceLocation::new).forEach(VALID_CHEST_LOOT::add);
    }

    @Override
    public void runWhenEnabled() {
        ItemHelper.ITEM_LIFETIME.put(ITEM, Integer.MAX_VALUE); // probably stupid

        // register loot function
        CHEST_LOOT_FUNCTION = CommonRegistry.lootFunctionType(CHEST_LOOT_ID, new LootItemFunctionType(new TotemOfPreservingChestLootFunction.Serializer()));
        MOB_LOOT_FUNCTION = CommonRegistry.lootFunctionType(MOB_LOOT_ID, new LootItemFunctionType(new TotemOfPreservingMobLootFunction.Serializer()));

        PlayerDropInventoryCallback.EVENT.register(this::handleDropInventory);
        EntityDropXpCallback.BEFORE.register(this::handleDropXp);
        LootTableEvents.MODIFY.register(this::handleLootTables);
    }

    private void handleLootTables(ResourceManager resourceManager, LootTables lootTables, ResourceLocation id, LootTable.Builder supplier, LootTableSource source) {
        if (VALID_MOB_LOOT.contains(id) || VALID_CHEST_LOOT.contains(id)) {
            var builder = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(Items.AIR)
                    .setWeight(1)
                    .apply(() -> {
                        if (VALID_CHEST_LOOT.contains(id)) {
                            return new TotemOfPreservingChestLootFunction(new LootItemCondition[0]);
                        } else {
                            return new TotemOfPreservingMobLootFunction(new LootItemCondition[0]);
                        }
                    }));

            supplier.withPool(builder);
        }
    }

    public InteractionResult handleDropXp(LivingEntity entity) {
        if (!preserveXp || !(entity instanceof Player) || entity.level.isClientSide) {
            return InteractionResult.PASS;
        }

        return InteractionResult.SUCCESS;
    }

    public InteractionResult handleDropInventory(Player player, Inventory inventory) {
        if (player.level.isClientSide()) return InteractionResult.PASS;

        var serverLevel = (ServerLevel) player.level;
        var serverPlayer = (ServerPlayer) player;
        var uuid = serverPlayer.getUUID();
        var message = serverPlayer.getScoreboardName();
        var random = serverPlayer.getRandom();
        var totemItem = new ItemStack(ITEM);
        var totemWorksFromInventory = Charm.LOADER.isEnabled(TotemWorksFromInventory.class);
        var inventories = ImmutableList.of(inventory.items, inventory.armor, inventory.offhand);
        List<ItemStack> itemsFromAllInventories = new ArrayList<>();

        inventories.forEach(list -> list.stream()
            .filter(Objects::nonNull)
            .filter(stack -> !stack.isEmpty())
            .forEach(itemsFromAllInventories::add));

        if (!graveMode && !totemWorksFromInventory) {
            // check if player is holding an empty totem - if not, exit early
            boolean holding = false;
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack held = player.getItemInHand(hand);
                if (held.getItem() == ITEM && !TotemOfPreservingItem.hasItems(totemItem)) {
                    holding = true;
                    break;
                }
            }

            if (!holding) {
                LogHelper.debug(getClass(), "No empty totem in hands (graveMode = false && totemWorksFromInventory = false), skipping");
                return InteractionResult.PASS;
            }
        }

        var foundEmptyTotem = false;
        List<ItemStack> preserve = new ArrayList<>();

        for (ItemStack stack : itemsFromAllInventories) {
            if (stack.getItem() == ITEM && !graveMode && !foundEmptyTotem) {
                LogHelper.debug(getClass(), "An empty totem was found (graveMode = false), going to try and add items to this totem");
                foundEmptyTotem = true;
                continue;
            }

            InteractionResult result = TotemOfPreservingEvents.BEFORE_ADD_STACK.invoker().invoke(serverPlayer, stack);
            if (result == InteractionResult.FAIL) continue;

            preserve.add(stack);
        }

        if (!foundEmptyTotem && !graveMode) {
            LogHelper.debug(getClass(), "No empty totem found (gravemode = false), giving up.");
            return InteractionResult.PASS;
        }

        // Don't spawn if there are no items to add.
        if (itemsFromAllInventories.isEmpty()) {
            LogHelper.debug(getClass(), "No items found to store in totem, giving up.");
            return InteractionResult.PASS;
        }

        // Get the death position.
        var pos = serverPlayer.blockPosition();
        var vehicle = serverPlayer.getVehicle();
        if (vehicle != null) {
            pos = vehicle.blockPosition();
        }

        // Do spawn checks.
        BlockPos spawnPos = null;
        var maxHeight = serverLevel.getMaxBuildHeight() - 1;
        var minHeight = serverLevel.getMinBuildHeight() + 1;
        var state = serverLevel.getBlockState(pos);
        var fluid = serverLevel.getFluidState(pos);

        // Adjust for void.
        if (pos.getY() < minHeight) {
            LogHelper.debug(getClass(), "(Void check) Adjusting, new pos: " + pos);
            pos = new BlockPos(pos.getX(), serverLevel.getSeaLevel(), pos.getZ());
        }

        if (state.isAir() || fluid.is(FluidTags.WATER)) {

            // Air and water are valid spawn positions.
            LogHelper.debug(getClass(), "(Standard check) Found an air/water block to spawn in: " + pos);
            spawnPos = pos;

        } else if (fluid.is(FluidTags.LAVA)) {

            // Lava: Keep moving upward to find air pocket, give up if solid (or void) reached.
            for (int tries = 0; tries < 20; tries++) {
                var y = pos.getY() + tries;
                if (y >= maxHeight) break;

                var tryPos = new BlockPos(pos.getX(), y, pos.getZ());
                var tryState = serverLevel.getBlockState(tryPos);
                var tryFluid = serverLevel.getFluidState(tryPos);
                if (tryFluid.is(FluidTags.LAVA)) continue;

                if (tryState.isAir() || tryFluid.is(FluidTags.WATER)) {
                    LogHelper.debug(getClass(), "(Lava check) Found an air/water block to spawn in after checking " + tries + " times: " + pos);
                    spawnPos = tryPos;
                }

                break;
            }

            // If that failed, replace the lava with the totem.
            if (spawnPos == null) {
                LogHelper.debug(getClass(), "(Lava check) Going to replace lava with totem at: " + pos);
                spawnPos = pos;
            }

        } else {

            // Solid block: Check above and nesw for an air or water pocket.
            List<Direction> directions = Arrays.asList(
                Direction.UP, Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH
            );
            for (var direction : directions) {
                var tryPos = pos.relative(direction);
                var tryState = serverLevel.getBlockState(tryPos);
                var tryFluid = serverLevel.getFluidState(tryPos);

                if (tryPos.getY() >= maxHeight) continue;
                if (tryState.isAir() || tryFluid.is(FluidTags.WATER)) {
                    LogHelper.debug(getClass(), "(Solid check) Found an air/water block to spawn in, direction: " + direction + ", pos: " + pos);
                    spawnPos = tryPos;
                    break;
                }
            }
        }

        if (spawnPos == null) {

            // Try and find a valid pos within 8 blocks of the death position.
            for (var tries = 0; tries < 8; tries++) {
                var x = pos.getX() + random.nextInt(tries + 1) - tries;
                var z = pos.getZ() + random.nextInt(tries + 1) - tries;

                // If upper void reached, count downward.
                var y = pos.getY() + tries;
                if (y > maxHeight) {
                    y = pos.getY() - tries;
                    if (y < minHeight) continue;
                }

                // Look for fluid and replacable solid blocks at increasing distance from the death point.
                var tryPos = new BlockPos(x, y, z);
                var tryState = serverLevel.getBlockState(tryPos);
                var tryFluid = serverLevel.getFluidState(tryPos);
                if (tryState.isAir() || tryFluid.is(FluidTags.WATER)) {
                    LogHelper.debug(getClass(), "(Distance check) Found an air/water block to spawn in after checking " + tries + " times: " + pos);
                    spawnPos = tryPos;
                    break;
                }
            }

        }

        if (spawnPos == null) {
            LogHelper.debug(getClass(), "Could not find a block to spawn totem, giving up.");
            return InteractionResult.PASS;
        }

        var x = spawnPos.getX();
        var y = spawnPos.getY();
        var z = spawnPos.getZ();

        serverLevel.setBlockAndUpdate(spawnPos, BLOCK.defaultBlockState());
        if (!(serverLevel.getBlockEntity(spawnPos) instanceof TotemBlockEntity totem)) {
            LogHelper.debug(getClass(), "Not a valid block entity at pos, giving up. Pos: " + pos);
            return InteractionResult.PASS;
        }

        if (preserveXp) {
            int xp = serverPlayer.totalExperience;
            LogHelper.debug(getClass(), "Preserving player XP in totem: " + xp);
            totem.setXp(xp);
        }

        totem.setItems(preserve);
        totem.setMessage(message);
        totem.setOwner(uuid);
        totem.setChanged();

        PROTECT_POSITIONS
            .computeIfAbsent(serverLevel.dimension().location(), a -> new ArrayList<>())
            .add(spawnPos);

        triggerUsedTotemOfPreserving(serverPlayer);
        LogHelper.info(getClass(), "Spawned a totem at: " + spawnPos);

        // Clear all player's inventories.
        for (NonNullList<ItemStack> inv : inventories) {
            Collections.fill(inv, ItemStack.EMPTY);
        }

        if (showDeathPosition) {
            player.displayClientMessage(TextHelper.translatable("gui.charm.totem_of_preserving.deathpos", x, y, z), false);
        }

        return InteractionResult.SUCCESS;
    }

    public static void triggerUsedTotemOfPreserving(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_USED_TOTEM_OF_PRESERVING);
    }
}
