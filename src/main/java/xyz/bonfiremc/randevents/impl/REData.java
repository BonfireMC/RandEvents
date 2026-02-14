package xyz.bonfiremc.randevents.impl;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;
import xyz.bonfiremc.randevents.api.resources.RandomEvent;
import xyz.bonfiremc.randevents.impl.resources.RandomEventManager;

import java.util.*;
import java.util.function.Function;

@ApiStatus.Internal
public class REData {
    private static final Codec<Map<Identifier, Identifier>> ID_MAP_CODEC = Codec.unboundedMap(Identifier.CODEC, Identifier.CODEC).xmap(HashMap::new, Function.identity());

    private final Map<RandomEvent.Trigger, Map<Identifier, Identifier>> values;
    private final long seed;

    public REData() {
        this.values = new HashMap<>();
        this.seed = 0;
    }

    public void write(CompoundTag nbt) {
        CompoundTag root = new CompoundTag();

        for (Map.Entry<RandomEvent.Trigger, Map<Identifier, Identifier>> entry : values.entrySet()) {
            root.put(entry.getKey().getSerializedName(), ID_MAP_CODEC.encodeStart(NbtOps.INSTANCE, entry.getValue()).getOrThrow());
        }

        nbt.put("RandomEvents", root);
    }

    public void read(CompoundTag nbt) {
        this.values.clear();

        for (Map.Entry<String, Tag> entry : nbt.entrySet()) {
            RandomEvent.Trigger trigger = RandomEvent.Trigger.valueOf(entry.getKey().toUpperCase());

            Map<Identifier, Identifier> ids = ID_MAP_CODEC.parse(NbtOps.INSTANCE, entry.getValue()).getOrThrow();

            this.values.put(trigger, ids);
        }
    }

    public Optional<RandomEvent> get(RandomEvent.Trigger trigger, Identifier id) {
        Map<Identifier, Identifier> ids = this.values.computeIfAbsent(trigger, ignored -> new HashMap<>());
        RandomEventManager manager = REInitializer.getRandomEventManager();

        Identifier eventId = ids.get(id);

        if (eventId != null) {
            Optional<RandomEvent> event = manager.get(eventId);

            if (event.isPresent()) {
                return event;
            } else {
                System.out.println("Random event with id \"" + id.toString() + "\" no longer exists, it was deleted?");
                ids.remove(id);
            }
        }

        List<Map.Entry<Identifier, RandomEvent>> candidates = manager.events.entrySet()
                .stream()
                .filter(entry -> entry.getValue().triggers().contains(trigger))
                .toList();

        if (candidates.isEmpty()) {
            return Optional.empty();
        }

        Random random = new Random(this.mixSeed(trigger, id));

        Map.Entry<Identifier, RandomEvent> randomEvent = candidates.get(random.nextInt(candidates.size()));
        ids.put(id, randomEvent.getKey());

        return Optional.of(randomEvent.getValue());
    }

    private long mixSeed(RandomEvent.Trigger trigger, Identifier id) {
        long h = this.seed;

        h = h * 31 + trigger.getSerializedName().hashCode();
        h = h * 31 + id.toString().hashCode();

        h ^= (h << 21);
        h ^= (h >>> 35);
        h ^= (h << 4);

        return h;
    }
}
