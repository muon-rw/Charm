package svenhjol.charm.feature.cooking_pots.common;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.charmony.common.helper.ItemOverrideHelper;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.cooking_pots.CookingPots;
import svenhjol.charm.feature.cooking_pots.common.dispenser.BowlBehavior;
import svenhjol.charm.feature.cooking_pots.common.dispenser.PotionBehavior;
import svenhjol.charm.feature.cooking_pots.common.dispenser.WaterBucketBehavior;

import java.util.List;
import java.util.function.Supplier;

public final class Registers extends RegisterHolder<CookingPots> {
    private static final String BLOCK_ID = "cooking_pot";
    private static final String MIXED_STEW_ID = "mixed_stew";

    public final Supplier<CookingPotBlock> block;
    public final Supplier<CookingPotBlock.BlockItem> blockItem;
    public final Supplier<BlockEntityType<CookingPotBlockEntity>> blockEntity;
    public final Supplier<MixedStewItem> mixedStewItem;
    public final Supplier<SoundEvent> addSound;
    public final Supplier<SoundEvent> ambientSound;
    public final Supplier<SoundEvent> finishSound;
    public final Supplier<SoundEvent> takeSound;
    public final FoodProperties mixedStewFoodProperties;

    public Registers(CookingPots feature) {
        super(feature);
        var registry = feature().registry();

        block = registry.block(BLOCK_ID, CookingPotBlock::new);
        blockItem = registry.item(BLOCK_ID, () -> new CookingPotBlock.BlockItem(block));
        blockEntity = registry.blockEntity(BLOCK_ID, () -> CookingPotBlockEntity::new, List.of(block));

        mixedStewFoodProperties = feature().handlers.buildFoodProperties();
        mixedStewItem = registry.item(MIXED_STEW_ID, MixedStewItem::new);

        addSound = registry.soundEvent("cooking_pot_add");
        ambientSound = registry.soundEvent("cooking_pot_ambient");
        finishSound = registry.soundEvent("cooking_pot_finish");
        takeSound = registry.soundEvent("cooking_pot_take");

        // Server to client packets
        registry.serverPacketSender(Networking.S2CAddedToCookingPot.TYPE,
            Networking.S2CAddedToCookingPot.CODEC);

    }

    @Override
    public void onEnabled() {
        var foodProperties = feature().handlers.buildFoodProperties();
        ItemOverrideHelper.dataComponentValue(mixedStewItem.get(), DataComponents.FOOD, foodProperties);
    }
}
