package xyz.bonfiremc.randevents.api.tags;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;
import xyz.bonfiremc.randevents.impl.REInitializer;

public class RETags {
    public static final TagKey<@NonNull Block> BLOCK_BREAK_BLACKLIST = create(Registries.BLOCK, "block_break_blacklist");
    public static final TagKey<@NonNull Block> BLOCK_PLACE_BLACKLIST = create(Registries.BLOCK, "block_place_blacklist");
    public static final TagKey<@NonNull EntityType<?>> ENTITY_KILL_BLACKLIST = create(Registries.ENTITY_TYPE, "entity_kill_blacklist");
    public static final TagKey<@NonNull Item> ITEM_CONSUME_BLACKLIST = create(Registries.ITEM, "item_consume_blacklist");

    private static <T> TagKey<@NonNull T> create(ResourceKey<@NonNull Registry<@NonNull T>> key, String path) {
        return TagKey.create(key, REInitializer.id(path));
    }
}
