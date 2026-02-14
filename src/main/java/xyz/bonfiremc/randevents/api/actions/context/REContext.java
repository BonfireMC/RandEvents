package xyz.bonfiremc.randevents.api.actions.context;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public interface REContext {
    @NonNull ServerPlayer player();

    @NonNull
    default Vec3 vec() {
        return this.player().position();
    }

    default void write(CompoundTag tag) {
        ServerPlayer player = this.player();

        tag.putDouble("player_x", player.getX());
        tag.putDouble("player_y", player.getY());
        tag.putDouble("player_z", player.getZ());
        tag.putString("player_uuid", player.getStringUUID());
        tag.putString("player_name", player.getPlainTextName());

        Vec3 vec = this.vec();

        tag.putDouble("x", vec.x);
        tag.putDouble("y", vec.y);
        tag.putDouble("z", vec.z);
    }

    @NonNull
    default CommandSourceStack createCommandSourceStack() {
        return this.player().createCommandSourceStack().withSuppressedOutput();
    }
}
