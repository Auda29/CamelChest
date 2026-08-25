package dev.wecke.camelchest.neoforge;

import dev.wecke.camelchest.CamelChest;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(CamelChest.MOD_ID)
public final class CamelChestNeoForge {
    public CamelChestNeoForge(IEventBus modBus) {
        CamelChestAttachments.register(modBus);
        CamelChest.initialize("NeoForge");
    }
}
