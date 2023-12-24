package svenhjol.charm.feature.improved_mineshafts;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.api.event.LevelLoadEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImprovedMineshafts extends CommonFeature {
    static final List<BlockState> FLOOR_BLOCKS = new ArrayList<>();
    static final List<BlockState> CEILING_BLOCKS = new ArrayList<>();
    static final List<BlockState> PILE_BLOCKS = new ArrayList<>();
    static final List<BlockState> ROOM_BLOCKS = new ArrayList<>();
    static final List<BlockState> ROOM_DECORATIONS = new ArrayList<>();
    static final List<ResourceLocation> MINECART_LOOT = new ArrayList<>();
    static final ResourceLocation FLOOR_BLOCK_LOOT = new ResourceLocation(Charm.ID, "improved_mineshafts/floor_blocks");
    static final ResourceLocation PILE_BLOCK_LOOT = new ResourceLocation(Charm.ID, "improved_mineshafts/pile_blocks");
    static final ResourceLocation CEILING_BLOCK_LOOT = new ResourceLocation(Charm.ID, "improved_mineshafts/ceiling_blocks");
    static final ResourceLocation ROOM_BLOCK_LOOT = new ResourceLocation(Charm.ID, "improved_mineshafts/room_blocks");
    static final ResourceLocation ROOM_DECORATION_LOOT = new ResourceLocation(Charm.ID, "improved_mineshafts/room_decorations");

    @Configurable(name = "Corridor floor blocks", description = "Chance (out of 1.0) of blocks such as candles and ores spawning on the floor of corridors.")
    public static double floorBlockChance = 0.03d;

    @Configurable(name = "Corridor ceiling blocks", description = "Chance (out of 1.0) of blocks such as lanterns spawning on the ceiling of corridors.")
    public static double ceilingBlockChance = 0.02d;

    @Configurable(name = "Corridor block piles", description = "Chance (out of 1.0) of stone, gravel and ore spawning at the entrance of corridors.")
    public static double blockPileChance = 0.2d;

    @Configurable(name = "Room blocks", description = "Chance (out of 1.0) for a moss or precious ore block to spawn on a single block of the central mineshaft room.")
    public static double roomBlockChance = 0.25d;

    @Configurable(name = "Extra minecarts", description = "Chance (out of 1.0) for a minecart to spawn in a corridor. Minecart loot is chosen from the 'Minecart loot tables'.")
    public static double minecartChance = 0.2d;

    @Override
    public String description() {
        return "Adds decoration and more ores to mineshafts.";
    }

    @Override
    public void register() {
        MINECART_LOOT.addAll(List.of(
            BuiltInLootTables.SIMPLE_DUNGEON,
            BuiltInLootTables.ABANDONED_MINESHAFT,
            BuiltInLootTables.VILLAGE_TEMPLE,
            BuiltInLootTables.VILLAGE_CARTOGRAPHER,
            BuiltInLootTables.VILLAGE_MASON,
            BuiltInLootTables.VILLAGE_TOOLSMITH,
            BuiltInLootTables.VILLAGE_WEAPONSMITH
        ));
    }

    @Override
    public void runWhenEnabled() {
        LevelLoadEvent.INSTANCE.handle(this::handleLevelLoad);
    }

    public static void generatePiece(StructurePiece piece, WorldGenLevel level, StructureManager accessor, ChunkGenerator chunkGenerator, RandomSource rand, BoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        // Don't add any decoration to mesa mineshafts.
        if (((MineshaftPieces.MineShaftPiece)piece).type == MineshaftStructure.Type.MESA) return;

        if (piece instanceof MineshaftPieces.MineShaftCorridor) {
            Generation.decorateCorridor((MineshaftPieces.MineShaftCorridor)piece, level, accessor, chunkGenerator, rand, box, chunkPos, blockPos);
        } else if (piece instanceof MineshaftPieces.MineShaftRoom) {
            Generation.decorateRoom((MineshaftPieces.MineShaftRoom)piece, level, accessor, chunkGenerator, rand, box, chunkPos, blockPos);
        }
    }

    private void handleLevelLoad(MinecraftServer server, ServerLevel level) {
        if (level.dimension() == Level.OVERWORLD) {
            // Do this only once.
            var builder = new LootParams.Builder(level);

            FLOOR_BLOCKS.addAll(parseLootTable(server, builder, FLOOR_BLOCK_LOOT));
            PILE_BLOCKS.addAll(parseLootTable(server, builder, PILE_BLOCK_LOOT));
            CEILING_BLOCKS.addAll(parseLootTable(server, builder, CEILING_BLOCK_LOOT));
            ROOM_BLOCKS.addAll(parseLootTable(server, builder, ROOM_BLOCK_LOOT));
            ROOM_DECORATIONS.addAll(parseLootTable(server, builder, ROOM_DECORATION_LOOT));
        }
    }

    private List<BlockState> parseLootTable(MinecraftServer server, LootParams.Builder builder, ResourceLocation tableName) {
        var lootTable = server.getLootData().getLootTable(tableName);
        var items = lootTable.getRandomItems(builder.create(LootContextParamSets.EMPTY));
        List<BlockState> states = new ArrayList<>();

        for (ItemStack stack : items) {
            BlockState state = getBlockStateFromItemStack(stack);
            states.add(state);
        }

        return states;
    }

    private BlockState getBlockStateFromItemStack(ItemStack stack) {
        var block = Block.byItem(stack.getItem());
        var state = block.defaultBlockState();
        var tag = stack.getTag();

        if (tag != null) {
            var definition = block.getStateDefinition();
            var blockStateTag = tag.getCompound(BlockItem.BLOCK_STATE_TAG);

            for (String key : blockStateTag.getAllKeys()) {
                var prop = definition.getProperty(key);
                if (prop == null) continue;
                var propString = Objects.requireNonNull(blockStateTag.get(key)).getAsString();
                state = BlockItem.updateState(state, prop, propString);
            }
        }

        return state;
    }
}
