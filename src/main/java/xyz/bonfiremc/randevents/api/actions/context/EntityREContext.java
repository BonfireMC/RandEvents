package xyz.bonfiremc.randevents.api.actions.context;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public record EntityREContext(@NonNull ServerPlayer player, @NonNull Entity entity) implements REContext {
    @Override
    public @NonNull Vec3 vec() {
        return this.entity.position();
    }

    @Override
    public void write(CompoundTag tag) {
        REContext.super.write(tag);

        tag.putDouble("entity_x", this.entity.getX());
        tag.putDouble("entity_y", this.entity.getY());
        tag.putDouble("entity_z", this.entity.getZ());
        tag.putString("entity_uuid", this.entity.getStringUUID());
        tag.putString("entity_name", this.entity.getPlainTextName());
        tag.putString("entity_id", this.entity.getType().builtInRegistryHolder().key().identifier().toString());
    }

    @Override
    public @NonNull CommandSourceStack createCommandSourceStack() {
        return this.entity.createCommandSourceStackForNameResolution(this.player.level()).withSuppressedOutput();
    }
}
