package dev.wecke.camelchest.fabric;

import dev.wecke.camelchest.CamelChest;
import net.fabricmc.api.ModInitializer;

public final class CamelChestFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CamelChest.initialize("Fabric");
    }
}
