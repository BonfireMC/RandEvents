package xyz.bonfiremc.randevents.impl.actions;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import org.jetbrains.annotations.ApiStatus;
import xyz.bonfiremc.randevents.api.actions.REAction;
import xyz.bonfiremc.randevents.api.actions.REActionType;
import xyz.bonfiremc.randevents.impl.REInitializer;

@ApiStatus.Internal
public class REActions {
    public static final REActionType EXECUTE_FUNCTION = register("execute_function", ExecuteFunctionREAction.CODEC);
    public static final REActionType RANDOM_PHRASE = register("random_phrase", MapCodec.unit(new RandomPhraseREAction()));
    public static final REActionType SHUFFLE_INVENTORY = register("shuffle_inventory", MapCodec.unit(new ShuffleInventoryREAction()));

    public static void load() {
    }

    private static REActionType register(String path, MapCodec<? extends REAction> action) {
        return Registry.register(REActionType.REGISTRY, REInitializer.id(path), new REActionType(action));
    }
}
