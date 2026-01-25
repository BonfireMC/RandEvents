package xyz.bonfiremc.randevents.api.actions.context;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record BlockREContext(@NonNull ServerPlayer player, @NonNull BlockPos blockPos, @NonNull BlockState blockState, @Nullable BlockEntity blockEntity) implements REContext {
    @Override
    public @NonNull ServerPlayer player() {
        return this.player;
    }

    @Override
    public @NonNull BlockPos preferablePos() {
        return this.blockPos;
    }
}
