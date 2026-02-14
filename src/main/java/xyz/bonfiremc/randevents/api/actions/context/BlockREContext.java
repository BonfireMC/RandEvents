package xyz.bonfiremc.randevents.api.actions.context;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record BlockREContext(@NonNull ServerPlayer player, @NonNull BlockPos blockPos, @NonNull BlockState blockState, @Nullable BlockEntity blockEntity) implements REContext {
    @Override
    public @NonNull Vec3 vec() {
        return this.blockPos.getCenter();
    }

    @Override
    public void write(CompoundTag tag) {
        REContext.super.write(tag);

        tag.putInt("block_x", this.blockPos.getX());
        tag.putInt("block_y", this.blockPos.getY());
        tag.putInt("block_z", this.blockPos.getZ());
        tag.putString("block_id", this.blockState.getBlock().builtInRegistryHolder().key().identifier().toString());
    }

    @Override
    public @NonNull CommandSourceStack createCommandSourceStack() {
        return REContext.super.createCommandSourceStack().withPosition(this.blockPos.getCenter());
    }
}
