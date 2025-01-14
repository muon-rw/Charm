package svenhjol.charm.feature.item_hover_sorting.common;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import svenhjol.charm.Charm;
import svenhjol.charm.charmony.enums.SortDirection;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.item_hover_sorting.ItemHoverSorting;

public final class Networking extends FeatureHolder<ItemHoverSorting> {
    public Networking(ItemHoverSorting feature) {
        super(feature);
    }

    // Client-to-server packet to set the slot index and direction of scroll.
    public record C2SScrollOnHover(int slotIndex, SortDirection sortDirection) implements CustomPacketPayload {
        static Type<C2SScrollOnHover> TYPE = new Type<>(Charm.id("item_hover_sorting_scroll"));
        static StreamCodec<FriendlyByteBuf, C2SScrollOnHover> CODEC
            = StreamCodec.of(C2SScrollOnHover::encode, C2SScrollOnHover::decode);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        private static void encode(FriendlyByteBuf buf, C2SScrollOnHover self) {
            buf.writeInt(self.slotIndex);
            buf.writeEnum(self.sortDirection);
        }

        private static C2SScrollOnHover decode(FriendlyByteBuf buf) {
            return new C2SScrollOnHover(buf.readInt(), buf.readEnum(SortDirection.class));
        }

        public static void send(int slotIndex, SortDirection sortDirection) {
            ClientPlayNetworking.send(new C2SScrollOnHover(slotIndex, sortDirection));
        }
    }
}
