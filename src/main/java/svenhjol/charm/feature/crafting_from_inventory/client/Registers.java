package svenhjol.charm.feature.crafting_from_inventory.client;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import svenhjol.charm.charmony.event.KeyPressEvent;
import svenhjol.charm.charmony.event.ScreenRenderEvent;
import svenhjol.charm.charmony.event.ScreenSetupEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.crafting_from_inventory.CraftingFromInventoryClient;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<CraftingFromInventoryClient> {
    public final Supplier<String> openPortableCraftingKey;

    public Registers(CraftingFromInventoryClient feature) {
        super(feature);

        openPortableCraftingKey = feature().registry().key("open_portable_crafting",
            () -> new KeyMapping("key.charm.open_portable_crafting", GLFW.GLFW_KEY_V, "key.categories.inventory"));
    }

    @Override
    public void onEnabled() {
        KeyPressEvent.INSTANCE.handle(feature().handlers::keyPress);
        ScreenSetupEvent.INSTANCE.handle(feature().handlers::screenSetup);
        ScreenRenderEvent.INSTANCE.handle(feature().handlers::screenRender);
    }
}
