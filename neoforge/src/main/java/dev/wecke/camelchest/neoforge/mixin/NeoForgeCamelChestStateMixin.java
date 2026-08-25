package dev.wecke.camelchest.neoforge.mixin;

import dev.wecke.camelchest.api.CamelChestAccess;
import dev.wecke.camelchest.neoforge.CamelChestAttachments;
import net.minecraft.world.entity.animal.camel.Camel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Camel.class)
public abstract class NeoForgeCamelChestStateMixin implements CamelChestAccess {
    @Override
    public boolean camelchest$hasChest() {
        return ((Camel) (Object) this).getData(CamelChestAttachments.HAS_CHEST);
    }

    @Override
    public void camelchest$setChest(boolean hasChest) {
        ((Camel) (Object) this).setData(CamelChestAttachments.HAS_CHEST, hasChest);
    }
}
