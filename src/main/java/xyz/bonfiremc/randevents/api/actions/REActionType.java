package xyz.bonfiremc.randevents.api.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NonNull;
import xyz.bonfiremc.randevents.impl.REInitializer;

public record REActionType(MapCodec<? extends REAction> codec) {
    public static final ResourceKey<@NonNull Registry<@NonNull REActionType>> RESOURCE_KEY = ResourceKey.createRegistryKey(REInitializer.id("action_types"));
    public static final Registry<@NonNull REActionType> REGISTRY = new MappedRegistry<>(RESOURCE_KEY, Lifecycle.stable());
    public static final Codec<REActionType> CODEC = REGISTRY.byNameCodec();
}
