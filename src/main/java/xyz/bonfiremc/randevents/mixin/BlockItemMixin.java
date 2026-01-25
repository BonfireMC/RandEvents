package xyz.bonfiremc.randevents.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.bonfiremc.randevents.api.event.player.PlayerBlockPlaceEvent;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V", shift = At.Shift.AFTER))
    public void place(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir, @Local BlockPos blockPos, @Local Level level, @Local Player player, @Local(ordinal = 1) BlockState blockState2) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        PlayerBlockPlaceEvent.EVENT.invoker()
                .onPlayerBlockPlace(level, serverPlayer, blockPos, blockState2, level.getBlockEntity(blockPos));
    }
}
