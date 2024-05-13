package svenhjol.charm.feature.smooth_glowstone.common;

import svenhjol.charm.Charm;
import svenhjol.charm.api.iface.IConditionalRecipe;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;
import svenhjol.charm.feature.smooth_glowstone.SmoothGlowstone;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.enums.Side;
import svenhjol.charm.foundation.feature.ProviderHolder;

import java.util.List;

public final class Providers extends ProviderHolder<SmoothGlowstone> implements IConditionalRecipeProvider {
    public Providers(SmoothGlowstone feature) {
        super(feature);
    }

    @Override
    public List<IConditionalRecipe> getRecipeConditions() {
        return List.of(
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return Resolve.isEnabled(Side.COMMON, Charm.id("firing"));
                }

                @Override
                public List<String> recipes() {
                    return List.of(
                        "smooth_glowstone/firing/*"
                    );
                }
            }
        );
    }
}
