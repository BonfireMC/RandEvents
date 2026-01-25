package xyz.bonfiremc.randevents.impl.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.bee.Bee;
import org.jetbrains.annotations.ApiStatus;
import xyz.bonfiremc.randevents.api.actions.REAction;
import xyz.bonfiremc.randevents.api.actions.REActionType;
import xyz.bonfiremc.randevents.api.actions.context.REContext;

@ApiStatus.Internal
public record BuzzyBeesREAction(int count) implements REAction {
    public static final MapCodec<BuzzyBeesREAction> CODEC = Codec.intRange(1, 1000).xmap(BuzzyBeesREAction::new, BuzzyBeesREAction::count).fieldOf("count");

    @Override
    public REActionType type() {
        return REActions.EXPLODE;
    }

    @Override
    public void execute(REContext context) {
        ServerPlayer player = context.player();
        BlockPos pos = context.preferablePos();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        for (int i = 0; i < this.count; i++) {
            Bee bee = new Bee(EntityType.BEE, player.level());

            bee.setPos(x, y, z);
            bee.setTimeToRemainAngry(Long.MAX_VALUE);

            bee.setPersistentAngerTarget(EntityReference.of(player));
            bee.setTarget(player);

            player.level().addFreshEntity(bee);
        }
    }
}
