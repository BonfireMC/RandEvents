package xyz.bonfiremc.randevents.impl.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;
import xyz.bonfiremc.randevents.api.actions.REAction;
import xyz.bonfiremc.randevents.api.actions.REActionType;
import xyz.bonfiremc.randevents.api.actions.context.REContext;

@ApiStatus.Internal
public record ExplodeAction(int radius) implements REAction {
    public static final MapCodec<ExplodeAction> CODEC = Codec.INT.xmap(ExplodeAction::new, ExplodeAction::radius).fieldOf("radius");

    @Override
    public REActionType type() {
        return REActions.EXPLODE;
    }

    @Override
    public void execute(REContext context) {
        ServerPlayer player = context.player();

        player.level().explode(null, player.getX(), player.getY(), player.getZ(), this.radius, Level.ExplosionInteraction.TNT);
    }
}
