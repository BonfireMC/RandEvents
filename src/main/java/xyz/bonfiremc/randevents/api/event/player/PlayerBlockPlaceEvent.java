package xyz.bonfiremc.randevents.api.event.player;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface PlayerBlockPlaceEvent {
    Event<@NonNull PlayerBlockPlaceEvent> EVENT = EventFactory.createArrayBacked(PlayerBlockPlaceEvent.class,
            callbacks -> ((level, player, blockPos, blockState, blockEntity) -> {
                for (PlayerBlockPlaceEvent callback : callbacks) {
                    callback.onPlayerBlockPlace(level, player, blockPos, blockState, blockEntity);
                }
            })
    );

    void onPlayerBlockPlace(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity);
}
