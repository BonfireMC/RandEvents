package xyz.bonfiremc.randevents.mixin;

import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.bonfiremc.randevents.impl.REData;
import xyz.bonfiremc.randevents.impl.duck.REDataProvider;

@Mixin(DerivedLevelData.class)
public class DerivedLevelDataMixin implements REDataProvider {
    @Shadow
    @Final
    private ServerLevelData wrapped;

    @Override
    public REData randevents$getREData() {
        return ((REDataProvider) this.wrapped).randevents$getREData();
    }
}
