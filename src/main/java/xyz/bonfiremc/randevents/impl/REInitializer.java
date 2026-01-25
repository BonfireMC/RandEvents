package xyz.bonfiremc.randevents.impl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;
import xyz.bonfiremc.randevents.api.actions.context.BlockREContext;
import xyz.bonfiremc.randevents.api.event.player.PlayerBlockPlaceEvent;
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
            this.triggerBlockEvent(RandomEvent.Trigger.BLOCK_BREAK, level, player, blockPos, blockState, blockEntity);
        });
        PlayerBlockPlaceEvent.EVENT.register((level, player, blockPos, blockState, blockEntity) -> {
            this.triggerBlockEvent(RandomEvent.Trigger.BLOCK_PLACE, level, player, blockPos, blockState, blockEntity);
        });
    }

    private void triggerBlockEvent(RandomEvent.Trigger trigger, Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        REData data = ((REDataProvider) level.getLevelData()).randevents$getREData();

        @SuppressWarnings("deprecation")
        Identifier blockId = blockState.getBlock().builtInRegistryHolder().key().identifier();

        data.get(trigger, level, blockId)
                .orElseThrow()
                .action()
                .execute(new BlockREContext(serverPlayer, blockPos, blockState, blockEntity));
    }
}
