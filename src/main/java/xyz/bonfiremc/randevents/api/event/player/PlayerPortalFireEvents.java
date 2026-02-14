package xyz.bonfiremc.randevents.api.event.player;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public interface PlayerPortalFireEvents {
    Event<@NonNull PlayerPortalFireEvents> NETHER = createNew();

    private static Event<@NonNull PlayerPortalFireEvents> createNew() {
        return EventFactory.createArrayBacked(PlayerPortalFireEvents.class,
                callbacks -> ((level, player, blockPos, blockState) -> {
                    for (PlayerPortalFireEvents callback : callbacks) {
                        callback.onPortalFire(level, player, blockPos, blockState);
                    }
                })
        );
    }

    void onPortalFire(Level level, Player player, BlockPos blockPos, BlockState blockState);
}
