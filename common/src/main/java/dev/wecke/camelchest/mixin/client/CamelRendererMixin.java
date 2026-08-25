package dev.wecke.camelchest.mixin.client;

import dev.wecke.camelchest.api.CamelChestAccess;
import dev.wecke.camelchest.client.CamelChestLayer;
import dev.wecke.camelchest.client.CamelChestRenderState;
import net.minecraft.client.model.animal.camel.CamelModel;
import net.minecraft.client.renderer.entity.CamelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.CamelRenderState;
import net.minecraft.world.entity.animal.camel.Camel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CamelRenderer.class)
public abstract class CamelRendererMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    @SuppressWarnings("unchecked")
    private void camelchest$addChestLayer(EntityRendererProvider.Context context, CallbackInfo callback) {
        RenderLayerParent<CamelRenderState, CamelModel> parent =
            (RenderLayerParent<CamelRenderState, CamelModel>) (Object) this;
        ((LivingEntityRendererAccessor) this).camelchest$addLayer(new CamelChestLayer(parent, context));
    }

    @Inject(
        method = "extractRenderState(Lnet/minecraft/world/entity/animal/camel/Camel;"
            + "Lnet/minecraft/client/renderer/entity/state/CamelRenderState;F)V",
        at = @At("TAIL")
    )
    private void camelchest$copyChestState(
        Camel camel,
        CamelRenderState state,
        float partialTick,
        CallbackInfo callback
    ) {
        boolean hasChest = ((CamelChestAccess) camel).camelchest$hasChest();
        ((CamelChestRenderState) state).camelchest$setHasChest(hasChest);
    }
}
