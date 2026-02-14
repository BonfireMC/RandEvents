package xyz.bonfiremc.randevents.impl.actions;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import xyz.bonfiremc.randevents.api.actions.REAction;
import xyz.bonfiremc.randevents.api.actions.REActionType;
import xyz.bonfiremc.randevents.api.actions.context.REContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApiStatus.Internal
public record ShuffleInventoryREAction() implements REAction {
    @Override
    public REActionType type() {
        return REActions.SHUFFLE_INVENTORY;
    }

    @Override
    public void execute(REContext context) {
        ServerPlayer player = context.player();
        Inventory inventory = player.getInventory();

        List<ItemStack> stacks = new ArrayList<>();

        for (int i = 0; i < 36; i++) {
            stacks.add(inventory.getItem(i));
        }

        Collections.shuffle(stacks);

        for (int i = 0; i < 36; i++) {
            inventory.setItem(i, stacks.get(i));
        }
    }
}
