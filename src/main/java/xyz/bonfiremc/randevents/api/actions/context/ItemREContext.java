package xyz.bonfiremc.randevents.api.actions.context;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public record ItemREContext(@NonNull ServerPlayer player, @NonNull ItemStack stack) implements REContext {
    @Override
    public void write(CompoundTag tag) {
        REContext.super.write(tag);

        tag.putString("item_id", this.stack.getItem().builtInRegistryHolder().key().identifier().toString());
        tag.putInt("item_count", this.stack.getCount());
    }
}
