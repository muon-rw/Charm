package svenhjol.charm.helper;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @version 1.0.1-charm
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class NetworkHelper {
    @Nullable
    public static FriendlyByteBuf encodeNbt(CompoundTag nbt) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            NbtIo.writeCompressed(nbt, out);
            String serialized = Base64.getEncoder().encodeToString(out.toByteArray());
            buffer.writeUtf(serialized);
            return buffer;
        } catch (IOException e) {
            LogHelper.warn(NetworkHelper.class, "Failed to compress nbt");
        }

        return null;
    }

    @Nullable
    public static CompoundTag decodeNbt(FriendlyByteBuf data) {
        try {
            byte[] byteData = Base64.getDecoder().decode(data.readUtf());
            return NbtIo.readCompressed(new ByteArrayInputStream(byteData));
        } catch (IOException e) {
            LogHelper.warn(NetworkHelper.class, "Failed to decompress nbt");
        }

        return null;
    }
}
