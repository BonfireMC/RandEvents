package xyz.bonfiremc.randevents.impl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.ApiStatus;
import xyz.bonfiremc.randevents.api.resources.RandomEvent;
import xyz.bonfiremc.randevents.impl.actions.REActions;
import xyz.bonfiremc.randevents.impl.duck.REDataProvider;
import xyz.bonfiremc.randevents.impl.resources.RandomEventManager;

@ApiStatus.Internal
public class REInitializer implements ModInitializer {
    public static final String MOD_ID = "randevents";

    private static RandomEventManager randomEventManager;

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static RandomEventManager getRandomEventManager() {
        return randomEventManager;
    }

    @Override
    public void onInitialize() {
        randomEventManager = new RandomEventManager();
        ResourceLoader.get(PackType.SERVER_DATA).registerReloader(RandomEventManager.ID, randomEventManager);

        REActions.load();

        PlayerBlockBreakEvents.AFTER.register((level, player, blockPos, blockState, blockEntity) -> {
            REData data = ((REDataProvider) level.getLevelData()).randevents$getREData();

            data.get(RandomEvent.Trigger.BLOCK_BREAK, level, blockState.getBlock().builtInRegistryHolder().key().identifier()).orElseThrow().action().execute(() -> (ServerPlayer) player);
        });
    }
}
