package xyz.bonfiremc.randevents.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.bonfiremc.randevents.impl.duck.REDataProvider;

@Mixin(LevelStorageSource.class)
public class LevelStorageSourceMixin {
    @WrapOperation(method = "getLevelDataAndDimensions", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/PrimaryLevelData;parse(Lcom/mojang/serialization/Dynamic;Lnet/minecraft/world/level/LevelSettings;Lnet/minecraft/world/level/storage/PrimaryLevelData$SpecialWorldProperty;Lnet/minecraft/world/level/levelgen/WorldOptions;Lcom/mojang/serialization/Lifecycle;)Lnet/minecraft/world/level/storage/PrimaryLevelData;"))
    private static <T> PrimaryLevelData getLevelDataAndDimensions(Dynamic<T> tag, LevelSettings levelSettings, PrimaryLevelData.SpecialWorldProperty specialWorldProperty, WorldOptions worldOptions, Lifecycle worldGenSettingsLifecycle, Operation<PrimaryLevelData> original) {
        PrimaryLevelData levelData = original.call(tag, levelSettings, specialWorldProperty, worldOptions, worldGenSettingsLifecycle);
        ((REDataProvider) levelData).randevents$getREData().read(tag.convert(NbtOps.INSTANCE).getValue().asCompound().orElseThrow().getCompoundOrEmpty("RandomEvents"));
        return levelData;
    }
}
