package xyz.bonfiremc.randevents.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.bonfiremc.randevents.api.event.player.PlayerConsumeEvent;

@Mixin(Consumable.class)
public class ConsumableMixin {
    @Inject(method = "onConsume", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"))
    public void onConsume(Level level, LivingEntity entity, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (entity instanceof Player player) {
            PlayerConsumeEvent.EVENT.invoker().onPlayerConsume(level, player, stack);
        }
    }
}
