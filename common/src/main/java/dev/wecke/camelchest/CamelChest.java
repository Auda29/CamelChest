package dev.wecke.camelchest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CamelChest {
    public static final String MOD_ID = "camelchest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private CamelChest() {
    }

    public static void initialize(String loader) {
        LOGGER.info("Camel Chest initialized on {}", loader);
    }
}
