package xyz.bonfiremc.randevents.impl;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import xyz.bonfiremc.randevents.api.resources.RandomEvent;
import xyz.bonfiremc.randevents.impl.resources.RandomEventManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class REData {
    private final Map<RandomEvent.Trigger, Map<Identifier, Identifier>> values;
    private final Codec<Map<Identifier, Identifier>> ID_MAP_CODEC = Codec.unboundedMap(Identifier.CODEC, Identifier.CODEC).xmap(HashMap::new, Function.identity());

    public REData() {
        this.values = new HashMap<>();
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

    public Optional<RandomEvent> get(RandomEvent.Trigger trigger, Level level, Identifier id) {
        Map<Identifier, Identifier> ids = this.values.computeIfAbsent(trigger, ignored -> new HashMap<>());

        Identifier eventId = ids.get(id);
        RandomEventManager manager = REInitializer.getRandomEventManager();

        if (eventId == null) {
            List<Map.Entry<Identifier, RandomEvent>> candidates = manager.events.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().triggers().contains(trigger))
                    .toList();

            if (candidates.isEmpty()) return Optional.empty();

            Map.Entry<Identifier, RandomEvent> random = candidates.get(level.random.nextInt(candidates.size()));

            ids.put(id, random.getKey());

            return Optional.of(random.getValue());
        } else {
            return manager.get(eventId);
        }
    }
}
