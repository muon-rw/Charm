package svenhjol.charm_core.fabric.mixin.accessor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(GiveGiftToHero.class)
public interface GiveGiftToHeroAccessor {
    @Mutable
    @Accessor("GIFTS")
    static Map<VillagerProfession, ResourceLocation> getGifts() {
        throw new AssertionError();
    }
}
