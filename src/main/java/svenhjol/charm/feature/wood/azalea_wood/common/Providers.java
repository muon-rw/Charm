package svenhjol.charm.feature.wood.azalea_wood.common;

import svenhjol.charm.api.iface.ConditionalAdvancement;
import svenhjol.charm.api.iface.ConditionalAdvancementProvider;
import svenhjol.charm.api.iface.ConditionalRecipe;
import svenhjol.charm.api.iface.ConditionalRecipeProvider;
import svenhjol.charm.feature.wood.azalea_wood.AzaleaWood;
import svenhjol.charm.feature.woodcutting.Woodcutting;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.charmony.feature.ProviderHolder;

import java.util.List;

public final class Providers extends ProviderHolder<AzaleaWood> implements ConditionalRecipeProvider, ConditionalAdvancementProvider {
    public Providers(AzaleaWood feature) {
        super(feature);
    }

    @Override
    public List<ConditionalRecipe> getRecipeConditions() {
        return List.of(
            new ConditionalRecipe() {
                @Override
                public boolean test() {
                    return Resolve.isEnabled(Woodcutting.class);
                }

                @Override
                public List<String> recipes() {
                    return List.of("azalea_wood/woodcutting/*");
                }
            }
        );
    }

    @Override
    public List<ConditionalAdvancement> getAdvancementConditions() {
        return List.of(
            new ConditionalAdvancement() {
                @Override
                public boolean test() {
                    return Resolve.isEnabled(Woodcutting.class);
                }

                @Override
                public List<String> advancements() {
                    return List.of("azalea_wood/recipes/woodcutting/*");
                }
            }
        );
    }
}
