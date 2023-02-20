package svenhjol.charm_core.forge.registry;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

public record DeferredModelLayerDefinition(
    Supplier<ModelLayerLocation> location,
    Supplier<LayerDefinition> definition
) { }
