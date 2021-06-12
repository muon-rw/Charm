package svenhjol.charm.module.variant_lanterns;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.block.CharmLanternBlock;
import svenhjol.charm.enums.IMetalMaterial;
import svenhjol.charm.enums.VanillaMetalMaterial;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.module.CharmModule;

import java.util.*;

@Module(mod = Charm.MOD_ID, client = VariantLanternsClient.class, description = "Variants lanterns crafted from vanilla metal nuggets and torches.")
public class VariantLanterns extends CharmModule {
    public static Map<IMetalMaterial, CharmLanternBlock> LANTERNS = new HashMap<>();

    @Override
    public void register() {
        for (IMetalMaterial material : VanillaMetalMaterial.getTypes()) {
            LANTERNS.put(material, new CharmLanternBlock(this, material.getSerializedName() + "_lantern"));
            LANTERNS.put(material, new CharmLanternBlock(this, material.getSerializedName() + "_soul_lantern"));
        }
    }

    @Override
    public List<ResourceLocation> getRecipesToRemove() {
        List<ResourceLocation> remove = new ArrayList<>();

        // remove lantern recipes if nuggets module is disabled
        if (!ModuleHandler.enabled("charm:extra_nuggets")) {
            remove.addAll(Arrays.asList(
                new ResourceLocation(Charm.MOD_ID, "variant_lanterns/copper_lantern"),
                new ResourceLocation(Charm.MOD_ID, "variant_lanterns/copper_soul_lantern"),
                new ResourceLocation(Charm.MOD_ID, "variant_lanterns/netherite_lantern"),
                new ResourceLocation(Charm.MOD_ID, "variant_lanterns/netherite_soul_lantern")
            ));
        }

        return remove;
    }
}
