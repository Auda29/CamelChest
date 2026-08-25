package dev.wecke.camelchest;

public final class CamelChestRules {
    public static final int STORAGE_COLUMNS = 5;

    private CamelChestRules() {
    }

    public static boolean canEquip(
        boolean baby,
        boolean hasChest,
        boolean secondaryUse,
        boolean holdingChest
    ) {
        return !baby && !hasChest && !secondaryUse && holdingChest;
    }

    public static int inventoryColumns(boolean hasChest) {
        return hasChest ? STORAGE_COLUMNS : 0;
    }
}
