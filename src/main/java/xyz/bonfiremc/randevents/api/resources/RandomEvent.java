package xyz.bonfiremc.randevents.api.resources;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;
import xyz.bonfiremc.randevents.api.actions.REAction;

import java.util.List;
import java.util.function.Function;

public record RandomEvent(List<Trigger> triggers, List<REAction> actions) {
    public static final Codec<RandomEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Trigger.CODEC.listOf().fieldOf("triggers").forGetter(RandomEvent::triggers),
            Codec.mapEither(
                    REAction.CODEC,
                    REAction.CODEC.codec().listOf().fieldOf("actions")
            ).xmap(
                    either -> either.map(List::of, Function.identity()),
                    actions -> actions.size() == 1
                            ? Either.left(actions.getFirst())
                            : Either.right(actions)
            ).forGetter(RandomEvent::actions)
    ).apply(instance, RandomEvent::new));

    public enum Trigger implements StringRepresentable {
        BLOCK_BREAK,
        BLOCK_PLACE,
        ENTITY_KILL,
        ITEM_CONSUME,
        NETHER_PORTAL,
        END_PORTAL;

        public static final Codec<Trigger> CODEC = StringRepresentable.fromEnum(Trigger::values);

        @Override
        public @NonNull String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}
