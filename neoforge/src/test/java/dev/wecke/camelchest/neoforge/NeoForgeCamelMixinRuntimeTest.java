package dev.wecke.camelchest.neoforge;

import dev.wecke.camelchest.api.CamelChestAccess;
import net.minecraft.world.entity.animal.camel.Camel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class NeoForgeCamelMixinRuntimeTest {
    @Test
    void appliesCamelChestMixinsAndRegistersAttachment() {
        assertTrue(CamelChestAccess.class.isAssignableFrom(Camel.class));
        assertDoesNotThrow(() -> Camel.class.getDeclaredMethod("getInventoryColumns"));
        assertDoesNotThrow(() -> Camel.class.getDeclaredMethod("getSlot", int.class));
        assertTrue(CamelChestAttachments.HAS_CHEST.isBound());
    }
}
