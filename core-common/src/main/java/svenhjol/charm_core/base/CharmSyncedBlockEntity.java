package svenhjol.charm_core.base;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public abstract class CharmSyncedBlockEntity extends BlockEntity {
    public CharmSyncedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
    }

    public CompoundTag getUpdateTag() {
        var updateTag = new CompoundTag();
        saveAdditional(updateTag);
        return updateTag;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        syncToClient();
    }

    public void syncToClient() {
        var level = getLevel();
        if (level != null && !level.isClientSide()) {
            syncBlockEntityToClient((ServerLevel)level, getBlockPos());
        }
    }

    public static void syncBlockEntityToClient(ServerLevel level, BlockPos pos) {
        level.getChunkSource().blockChanged(pos);
    }
}
