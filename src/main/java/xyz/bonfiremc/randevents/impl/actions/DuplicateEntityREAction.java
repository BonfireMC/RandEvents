package xyz.bonfiremc.randevents.impl.actions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import xyz.bonfiremc.randevents.api.actions.REAction;
import xyz.bonfiremc.randevents.api.actions.REActionType;
import xyz.bonfiremc.randevents.api.actions.context.EntityREContext;
import xyz.bonfiremc.randevents.api.actions.context.REContext;

public record DuplicateEntityREAction() implements REAction {
    @Override
    public REActionType type() {
        return REActions.DUPLICATE_ENTITY;
    }

    @Override
    public void execute(REContext context) {
        if (!(context instanceof EntityREContext entityContext)) return;

        Entity entity = entityContext.entity();
        BlockPos pos = context.preferablePos();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        for (int i = 0; i < 2; i++) {
            Entity newEntity = entity.getType().create(entity.level(), EntitySpawnReason.COMMAND);

            if (newEntity != null) {
                newEntity.setPos(x, y, z);
                entity.level().addFreshEntity(newEntity);
            }
        }
    }
}
