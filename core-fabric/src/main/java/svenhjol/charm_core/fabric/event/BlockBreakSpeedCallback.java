package svenhjol.charm_core.fabric.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockBreakSpeedCallback {
    Event<BlockBreakSpeedCallback> EVENT = EventFactory.createArrayBacked(BlockBreakSpeedCallback.class, 
        listeners -> ((player, state, currentSpeed) -> {
            float speed = currentSpeed;
            
            for (BlockBreakSpeedCallback listener : listeners) {
                speed = listener.interact(player, state, speed);
            }
            
            return speed;
        }));
    
    float interact(Player player, BlockState state, float currentSpeed);
}