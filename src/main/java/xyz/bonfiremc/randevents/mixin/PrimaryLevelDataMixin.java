package xyz.bonfiremc.randevents.mixin;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.bonfiremc.randevents.impl.REData;
import xyz.bonfiremc.randevents.impl.duck.REDataProvider;

@Mixin(PrimaryLevelData.class)
public class PrimaryLevelDataMixin implements REDataProvider {
    @Unique
    private final REData reData = new REData();

    @Inject(method = "setTagData", at = @At(value = "RETURN"))
    public void setTagData(RegistryAccess registry, CompoundTag nbt, CompoundTag playerNBT, CallbackInfo ci) {
        this.reData.write(nbt);
    }

    @Override
    public REData randevents$getREData() {
        return this.reData;
    }
}
