package svenhjol.charm_api.iface;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IRemovesAdvancements {
    List<ResourceLocation> getAdvancementsToRemove();
}
