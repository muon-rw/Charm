package svenhjol.charm.module.variant_barrels;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.enums.IWoodMaterial;
import svenhjol.charm.enums.VanillaWoodMaterial;
import svenhjol.charm.helper.WorldHelper;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.CommonRegistry;

import java.util.*;

@CommonModule(mod = Charm.MOD_ID, priority = 10, description = "Barrels available in all types of vanilla wood.")
public class VariantBarrels extends CharmModule {
    public static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "barrel");
    public static final Map<IWoodMaterial, VariantBarrelBlock> BARREL_BLOCKS = new HashMap<>();

    @Override
    public void register() {
        for (VanillaWoodMaterial type : VanillaWoodMaterial.values()) {
            registerBarrel(this, type);
        }
    }

    public static void registerBarrel(CharmModule module, IWoodMaterial material) {
        VariantBarrelBlock barrel = new VariantBarrelBlock(module, material);
        BARREL_BLOCKS.put(material, barrel);
        CommonRegistry.addBlocksToBlockEntity(BlockEntityType.BARREL, barrel);
        WorldHelper.addBlockStatesToPointOfInterest(PoiType.FISHERMAN, barrel.getStateDefinition().getPossibleStates());
    }

    public static VariantBarrelBlock getRandomBarrel(Random rand) {
        List<VariantBarrelBlock> values = new ArrayList<>(BARREL_BLOCKS.values());
        return values.get(rand.nextInt(values.size()));
    }
}
