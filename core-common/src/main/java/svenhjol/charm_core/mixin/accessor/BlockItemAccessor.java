package svenhjol.charm_core.mixin.accessor;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockItem.class)
public interface BlockItemAccessor {
    @Mutable
    @Accessor("block")
    void setBlock(Block block);

    @Invoker("updateState")
    static <T extends Comparable<T>> BlockState invokeUpdateState(BlockState state, Property<T> property, String name) {
        throw new AssertionError();
    }
}
