package xyz.bonfiremc.randevents.impl.actions;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import org.jetbrains.annotations.ApiStatus;
import xyz.bonfiremc.randevents.api.actions.REAction;
import xyz.bonfiremc.randevents.api.actions.REActionType;
import xyz.bonfiremc.randevents.impl.REInitializer;

@ApiStatus.Internal
public class REActions {
    public static final REActionType EXPLODE = register("explode", ExplodeREAction.CODEC);
    public static final REActionType SPAWN_ENTITY = register("spawn_entity", SpawnEntityREAction.CODEC);
    public static final REActionType DUPLICATE_ENTITY = register("duplicate_entity", new DuplicateEntityREAction());

    public static void load() {
    }

    private static REActionType register(String path, MapCodec<? extends REAction> action) {
        return Registry.register(REActionType.REGISTRY, REInitializer.id(path), new REActionType(action));
    }

    private static <T extends REAction> REActionType register(String path, T action) {
        return register(path, MapCodec.unit(action));
    }
}
