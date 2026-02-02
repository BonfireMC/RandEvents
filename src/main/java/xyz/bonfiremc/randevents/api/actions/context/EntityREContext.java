package xyz.bonfiremc.randevents.api.actions.context;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.NonNull;

public record EntityREContext(@NonNull ServerPlayer player, @NonNull Entity entity) implements REContext {
    @Override
    public @NonNull BlockPos preferablePos() {
        return entity.blockPosition();
    }
}
