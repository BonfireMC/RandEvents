package xyz.bonfiremc.randevents.impl;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class RandEvents implements ModInitializer {
    public static final String MOD_ID = "randevents";

    @Override
    public void onInitialize() {
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
