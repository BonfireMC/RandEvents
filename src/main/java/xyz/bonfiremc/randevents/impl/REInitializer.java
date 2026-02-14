package xyz.bonfiremc.randevents.impl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;
import xyz.bonfiremc.randevents.api.actions.REAction;
import xyz.bonfiremc.randevents.api.actions.context.BlockREContext;
import xyz.bonfiremc.randevents.api.actions.context.EntityREContext;
import xyz.bonfiremc.randevents.api.actions.context.ItemREContext;
import xyz.bonfiremc.randevents.api.actions.context.REContext;
import xyz.bonfiremc.randevents.api.event.player.PlayerBlockPlaceEvent;
import xyz.bonfiremc.randevents.api.event.player.PlayerConsumeEvent;
import xyz.bonfiremc.randevents.api.event.player.PlayerPortalFireEvents;
import xyz.bonfiremc.randevents.api.resources.RandomEvent;
import xyz.bonfiremc.randevents.api.tags.RETags;
import xyz.bonfiremc.randevents.impl.actions.REActions;
import xyz.bonfiremc.randevents.impl.duck.REDataProvider;
import xyz.bonfiremc.randevents.impl.resources.RandomEventManager;

import java.util.Optional;

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
            if (level.isClientSide() || blockState.is(RETags.BLOCK_BREAK_BLACKLIST)) return;

            this.triggerEvent(
                    RandomEvent.Trigger.BLOCK_BREAK,
                    level,
                    blockState.getBlock().builtInRegistryHolder(),
                    new BlockREContext((ServerPlayer) player, blockPos, blockState, blockEntity)
            );
        });
        PlayerBlockPlaceEvent.EVENT.register((level, player, blockPos, blockState, blockEntity) -> {
            if (level.isClientSide() || blockState.is(RETags.BLOCK_PLACE_BLACKLIST)) return;

            this.triggerEvent(
                    RandomEvent.Trigger.BLOCK_PLACE,
                    level,
                    blockState.getBlock().builtInRegistryHolder(),
                    new BlockREContext((ServerPlayer) player, blockPos, blockState, blockEntity)
            );
        });
        ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, damageSource) -> {
            if (livingEntity.level().isClientSide() || !(damageSource.getEntity() instanceof ServerPlayer player) || livingEntity.getType().is(RETags.ENTITY_KILL_BLACKLIST)) return;

            this.triggerEvent(
                    RandomEvent.Trigger.ENTITY_KILL,
                    livingEntity.level(),
                    livingEntity.getType().builtInRegistryHolder(),
                    new EntityREContext(player, livingEntity)
            );
        });
        PlayerConsumeEvent.EVENT.register((level, player, stack) -> {
            if (level.isClientSide() || stack.is(RETags.ITEM_CONSUME_BLACKLIST)) return;

            this.triggerEvent(
                    RandomEvent.Trigger.ITEM_CONSUME,
                    level,
                    stack.getItem().builtInRegistryHolder(),
                    new ItemREContext((ServerPlayer) player, stack)
            );
        });
        PlayerPortalFireEvents.NETHER.register((level, player, blockPos, blockState) -> {
            if (level.isClientSide()) return;

            this.triggerEvent(
                    RandomEvent.Trigger.NETHER_PORTAL,
                    level,
                    blockState.getBlock().builtInRegistryHolder(),
                    new BlockREContext((ServerPlayer) player, blockPos, blockState, null)
            );
        });
    }

    private void triggerEvent(RandomEvent.Trigger trigger, Level level, Holder.Reference<?> id, REContext ctx) {
        REData data = ((REDataProvider) level.getLevelData()).randevents$getREData();
        Optional<RandomEvent> event = data.get(trigger, id.key().identifier());

        if (event.isPresent()) {
            for (REAction action : event.get().actions()) {
                action.execute(ctx);
            }
        }
    }
}
