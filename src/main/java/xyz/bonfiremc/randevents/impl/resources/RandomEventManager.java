package xyz.bonfiremc.randevents.impl.resources;

import net.fabricmc.fabric.impl.resource.FabricResourceReloader;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;
import xyz.bonfiremc.randevents.api.resources.RandomEvent;
import xyz.bonfiremc.randevents.impl.REInitializer;

import java.util.Map;
import java.util.Optional;

@ApiStatus.Internal
public class RandomEventManager extends SimpleJsonResourceReloadListener<@NonNull RandomEvent> implements FabricResourceReloader {
    public static final Identifier ID = REInitializer.id("random_events");
    private static final FileToIdConverter ASSET_LISTER = FileToIdConverter.json("random_event");
    public Map<Identifier, RandomEvent> events = Map.of();

    public RandomEventManager() {
        super(RandomEvent.CODEC, ASSET_LISTER);
    }

    protected void apply(Map<Identifier, RandomEvent> map, @NonNull ResourceManager resourceManager, @NonNull ProfilerFiller profilerFiller) {
        this.events = map;
    }

    public Optional<RandomEvent> get(Identifier id) {
        return Optional.ofNullable(this.events.get(id));
    }

    @Override
    public @NonNull Identifier fabric$getId() {
        return ID;
    }
}
