package xyz.bonfiremc.randevents.impl.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;
import xyz.bonfiremc.randevents.api.actions.REAction;
import xyz.bonfiremc.randevents.api.actions.REActionType;
import xyz.bonfiremc.randevents.api.actions.context.REContext;

@ApiStatus.Internal
public record ExplodeREAction(int radius, Level.ExplosionInteraction explosionInteraction) implements REAction {
    public static final MapCodec<ExplodeREAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("radius").forGetter(ExplodeREAction::radius),
            Level.ExplosionInteraction.CODEC.optionalFieldOf("explosion_interaction", Level.ExplosionInteraction.TNT).forGetter(ExplodeREAction::explosionInteraction)
    ).apply(instance, ExplodeREAction::new));

    @Override
    public REActionType type() {
        return REActions.EXPLODE;
    }

    @Override
    public void execute(REContext context) {
        ServerPlayer player = context.player();
        BlockPos pos = context.preferablePos();

        player.level().explode(null, pos.getX(), pos.getY(), pos.getZ(), this.radius, this.explosionInteraction);
    }
}
