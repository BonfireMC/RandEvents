package xyz.bonfiremc.randevents.impl.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import org.jetbrains.annotations.ApiStatus;
import xyz.bonfiremc.randevents.api.actions.REAction;
import xyz.bonfiremc.randevents.api.actions.REActionType;
import xyz.bonfiremc.randevents.api.actions.context.REContext;

@ApiStatus.Internal
public record SpawnEntityREAction(EntityType<?> entityType, int count, boolean angryAtPlayer) implements REAction {
    public static final MapCodec<SpawnEntityREAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EntityType.CODEC.fieldOf("entity_type").forGetter(SpawnEntityREAction::entityType),
            Codec.intRange(1, 1000).optionalFieldOf("count", 1).forGetter(SpawnEntityREAction::count),
            Codec.BOOL.optionalFieldOf("angry_at_player", false).forGetter(SpawnEntityREAction::angryAtPlayer)
    ).apply(instance, SpawnEntityREAction::new));

    @Override
    public REActionType type() {
        return REActions.SPAWN_ENTITY;
    }

    @Override
    public void execute(REContext context) {
        ServerPlayer player = context.player();
        BlockPos pos = context.preferablePos();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        for (int i = 0; i < this.count; i++) {
            Entity entity = this.entityType.create(player.level(), EntitySpawnReason.COMMAND);

            if (entity == null) return;

            entity.setPos(x, y, z);

            if (this.angryAtPlayer && entity instanceof NeutralMob mob) {
                mob.setTimeToRemainAngry(Long.MAX_VALUE);
                mob.setTarget(player);
            }

            player.level().addFreshEntity(entity);
        }
    }
}
