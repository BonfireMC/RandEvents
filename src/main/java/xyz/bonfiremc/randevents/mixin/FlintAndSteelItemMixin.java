package xyz.bonfiremc.randevents.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.bonfiremc.randevents.api.event.player.PlayerPortalFireEvents;

@Mixin(FlintAndSteelItem.class)
public class FlintAndSteelItemMixin {
    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/criterion/ItemUsedOnLocationTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V"))
    public void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir, @Local Level level, @Local(ordinal = 1) BlockPos blockPos2) {
        BlockState state = level.getBlockState(blockPos2);

        if (state.is(Blocks.NETHER_PORTAL)) {
            PlayerPortalFireEvents.NETHER.invoker().onPortalFire(level, context.getPlayer(), blockPos2, state);
        }
    }
}
