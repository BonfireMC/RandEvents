package xyz.bonfiremc.randevents.api.event.player;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public interface PlayerConsumeEvent {
    Event<@NonNull PlayerConsumeEvent> EVENT = EventFactory.createArrayBacked(PlayerConsumeEvent.class,
            callbacks -> ((level, player, stack) -> {
                for (PlayerConsumeEvent callback : callbacks) {
                    callback.onPlayerConsume(level, player, stack);
                }
            })
    );

    void onPlayerConsume(Level level, Player player, ItemStack stack);
}
