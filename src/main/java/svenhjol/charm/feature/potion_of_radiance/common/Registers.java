package svenhjol.charm.feature.potion_of_radiance.common;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import svenhjol.charm.feature.potion_of_radiance.PotionOfRadiance;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<PotionOfRadiance> {
    public final Supplier<Holder<Potion>> potion;
    public final Supplier<Holder<Potion>> longPotion;

    public Registers(PotionOfRadiance feature) {
        super(feature);
        var registry = feature.registry();

        potion = registry.potion("charm_radiance", RadiancePotion::new);
        longPotion = registry.potion("charm_long_radiance", LongRadiancePotion::new);

        registry.brewingRecipe(
            Potions.AWKWARD,
            () -> Items.TORCHFLOWER,
            potion.get());

        registry.brewingRecipe(
            potion.get(),
            () -> Items.REDSTONE,
            longPotion.get());
    }
}