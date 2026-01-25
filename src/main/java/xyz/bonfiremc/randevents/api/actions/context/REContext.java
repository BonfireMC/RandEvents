package xyz.bonfiremc.randevents.api.actions.context;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.NonNull;

public interface REContext {
    @NonNull ServerPlayer player();

    @NonNull
    default BlockPos preferablePos() {
        return this.player().blockPosition();
    }
}
