package dev.wecke.camelchest.mixin.client;

import dev.wecke.camelchest.client.CamelChestRenderState;
import net.minecraft.client.renderer.entity.state.CamelRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CamelRenderState.class)
public final class CamelRenderStateMixin implements CamelChestRenderState {
    @Unique
    private boolean camelchest$hasChest;

    @Override
    public boolean camelchest$hasChest() {
        return camelchest$hasChest;
    }

    @Override
    public void camelchest$setHasChest(boolean hasChest) {
        camelchest$hasChest = hasChest;
    }
}
