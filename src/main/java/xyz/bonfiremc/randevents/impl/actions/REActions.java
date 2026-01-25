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
    public static final REActionType BUZZY_BEES = register("buzzy_bees", BuzzyBeesREAction.CODEC);

    public static void load() {
    }

    private static REActionType register(String path, MapCodec<? extends REAction> action) {
        return Registry.register(REActionType.REGISTRY, REInitializer.id(path), new REActionType(action));
    }
}
