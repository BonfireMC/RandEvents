package xyz.bonfiremc.randevents.impl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.ItemEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.ApiStatus;
import xyz.bonfiremc.randevents.api.actions.context.BlockREContext;
import xyz.bonfiremc.randevents.api.actions.context.EntityREContext;
import xyz.bonfiremc.randevents.api.actions.context.ItemREContext;
import xyz.bonfiremc.randevents.api.actions.context.REContext;
import xyz.bonfiremc.randevents.api.event.player.PlayerBlockPlaceEvent;
import xyz.bonfiremc.randevents.api.resources.RandomEvent;
import xyz.bonfiremc.randevents.impl.actions.REActions;
import xyz.bonfiremc.randevents.impl.duck.REDataProvider;
import xyz.bonfiremc.randevents.impl.resources.RandomEventManager;

import java.util.List;

@ApiStatus.Internal
public class REInitializer implements ModInitializer {
    public static final String MOD_ID = "randevents";
    // TODO: convert into tag
    private static final List<Block> IGNORED_BLOCKS = List.of(
            Blocks.CRAFTING_TABLE,
            Blocks.CRAFTER,
            Blocks.STONECUTTER,
            Blocks.LOOM,
            Blocks.CARTOGRAPHY_TABLE,
            Blocks.FURNACE,
            Blocks.BLAST_FURNACE,
            Blocks.SMOKER,
            Blocks.SMITHING_TABLE,
            Blocks.ANVIL,
            Blocks.ENCHANTING_TABLE,
            Blocks.GRINDSTONE,
            Blocks.FLETCHING_TABLE, // don't ask
            Blocks.BARREL,
            Blocks.LANTERN,
            Blocks.COMPOSTER,
            Blocks.CAULDRON,
            Blocks.BREWING_STAND
    );
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
            if (level.isClientSide() || IGNORED_BLOCKS.contains(blockState.getBlock())) return;

            this.triggerEvent(
                    RandomEvent.Trigger.BLOCK_BREAK,
                    level,
                    blockState.getBlock().builtInRegistryHolder(),
                    new BlockREContext((ServerPlayer) player, blockPos, blockState, blockEntity)
            );
        });
        PlayerBlockPlaceEvent.EVENT.register((level, player, blockPos, blockState, blockEntity) -> {
            if (level.isClientSide() || IGNORED_BLOCKS.contains(blockState.getBlock())) return;

            this.triggerEvent(
                    RandomEvent.Trigger.BLOCK_PLACE,
                    level,
                    blockState.getBlock().builtInRegistryHolder(),
                    new BlockREContext((ServerPlayer) player, blockPos, blockState, blockEntity)
            );
        });
        ItemEvents.USE.register((level, player, interactionHand) -> {
            ItemStack stack = player.getItemInHand(interactionHand);

            if (!level.isClientSide() && !(stack.getItem() instanceof BlockItem)) {
                this.triggerEvent(
                        RandomEvent.Trigger.ITEM_USE,
                        level,
                        stack.getItem().builtInRegistryHolder(),
                        new ItemREContext((ServerPlayer) player, stack)
                );
            }

            return InteractionResult.PASS;
        });
        ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, damageSource) -> {
            if (livingEntity.level().isClientSide() || !(damageSource.getEntity() instanceof ServerPlayer player)) return;

            this.triggerEvent(
                    RandomEvent.Trigger.ENTITY_KILL,
                    livingEntity.level(),
                    livingEntity.getType().builtInRegistryHolder(),
                    new EntityREContext(player, livingEntity)
            );
        });
    }

    private void triggerEvent(RandomEvent.Trigger trigger, Level level, Holder.Reference<?> id, REContext ctx) {
        REData data = ((REDataProvider) level.getLevelData()).randevents$getREData();

        data.get(trigger, level, id.key().identifier())
                .orElseThrow()
                .action()
                .execute(ctx);
    }
}
