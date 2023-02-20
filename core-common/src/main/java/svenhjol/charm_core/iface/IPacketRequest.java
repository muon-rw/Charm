package svenhjol.charm_core.iface;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm_core.annotation.Packet;
import svenhjol.charm_core.enums.PacketDirection;

public interface IPacketRequest {
    default void encode(FriendlyByteBuf buf) {
        // no op
    }

    default void decode(FriendlyByteBuf buf) {
        // no op
    }

    default ResourceLocation id() {
        ResourceLocation id;

        if (getClass().isAnnotationPresent(Packet.class)) {
            var annotation = getClass().getAnnotation(Packet.class);
            id = new ResourceLocation(annotation.id());
        } else {
            throw new IllegalStateException("Missing ID for `" + getClass() + "`");
        }

        return id;
    }

    default PacketDirection direction() {
        PacketDirection direction;

        if (getClass().isAnnotationPresent(Packet.class)) {
            var annotation = getClass().getAnnotation(Packet.class);
            direction = annotation.direction();
        } else {
            throw new IllegalStateException("Missing packet direction for `" + getClass() + "`");
        }

        return direction;
    }
}
