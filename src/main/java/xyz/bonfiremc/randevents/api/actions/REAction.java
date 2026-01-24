package xyz.bonfiremc.randevents.api.actions;

import com.mojang.serialization.MapCodec;
import xyz.bonfiremc.randevents.api.actions.context.REContext;

public interface REAction {
    MapCodec<REAction> CODEC = REActionType.CODEC.dispatchMap("action", REAction::type, REActionType::codec);

    REActionType type();

    void execute(REContext context);
}
