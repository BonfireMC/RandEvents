package xyz.bonfiremc.randevents.api.resources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;
import xyz.bonfiremc.randevents.api.actions.REAction;

import java.util.List;

public record RandomEvent(List<Trigger> triggers, REAction action) {
    public static final Codec<RandomEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Trigger.CODEC.listOf().fieldOf("triggers").forGetter(RandomEvent::triggers),
            REAction.CODEC.forGetter(RandomEvent::action)
    ).apply(instance, RandomEvent::new));

    public enum Trigger implements StringRepresentable {
        BLOCK_BREAK,
        BLOCK_PLACE,
        ITEM_USE,
        ENTITY_KILL;

        public static final Codec<Trigger> CODEC = StringRepresentable.fromEnum(Trigger::values);

        @Override
        public @NonNull String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}
