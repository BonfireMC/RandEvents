package xyz.bonfiremc.randevents.api.actions.context;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public record ItemREContext(@NonNull ServerPlayer player, @NonNull ItemStack stack) implements REContext {
}
