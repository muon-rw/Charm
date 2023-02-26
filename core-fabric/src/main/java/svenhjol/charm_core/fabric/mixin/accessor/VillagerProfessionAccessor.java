package svenhjol.charm_core.fabric.mixin.accessor;

import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(VillagerProfession.class)
public interface VillagerProfessionAccessor {
    @Invoker("register")
    static VillagerProfession invokeRegister(String id, ResourceKey<PoiType> poitKey, ImmutableSet<Item> harvestableItems, ImmutableSet<Block> secondaryJobSites, @Nullable SoundEvent workSound) {
        throw new AssertionError();
    }
}