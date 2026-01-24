package xyz.bonfiremc.randevents.impl.duck;

import org.jetbrains.annotations.ApiStatus;
import xyz.bonfiremc.randevents.impl.REData;

@ApiStatus.Internal
public interface REDataProvider {
    REData randevents$getREData();
}
