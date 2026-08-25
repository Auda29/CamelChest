package dev.wecke.camelchest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

final class CamelChestRulesTest {
    @Test
    void adultCamelCanEquipChestDuringNormalInteraction() {
        assertTrue(CamelChestRules.canEquip(false, false, false, true));
    }

    @Test
    void chestCannotBeEquippedTwice() {
        assertFalse(CamelChestRules.canEquip(false, true, false, true));
    }

    @Test
    void secondaryUseStaysAvailableForOpeningInventory() {
        assertFalse(CamelChestRules.canEquip(false, false, true, true));
    }

    @Test
    void babiesAndNonChestItemsAreRejected() {
        assertFalse(CamelChestRules.canEquip(true, false, false, true));
        assertFalse(CamelChestRules.canEquip(false, false, false, false));
    }

    @Test
    void attachedChestAddsFifteenStorageSlots() {
        assertEquals(5, CamelChestRules.inventoryColumns(true));
        assertEquals(0, CamelChestRules.inventoryColumns(false));
        assertEquals(15, CamelChestRules.inventoryColumns(true) * 3);
    }
}
