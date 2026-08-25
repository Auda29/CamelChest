package dev.wecke.camelchest.neoforge;

import dev.wecke.camelchest.CamelChest;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class CamelChestAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, CamelChest.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> HAS_CHEST =
        ATTACHMENT_TYPES.register(
            "has_chest",
            () -> AttachmentType.builder(() -> false)
                .sync(ByteBufCodecs.BOOL)
                .build()
        );

    private CamelChestAttachments() {
    }

    public static void register(IEventBus modBus) {
        ATTACHMENT_TYPES.register(modBus);
    }
}
