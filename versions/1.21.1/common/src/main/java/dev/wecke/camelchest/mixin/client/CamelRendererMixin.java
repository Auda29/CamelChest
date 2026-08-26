package dev.wecke.camelchest.mixin.client;

import dev.wecke.camelchest.client.CamelChestLayer;
import net.minecraft.client.model.CamelModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.CamelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.animal.camel.Camel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CamelRenderer.class)
public abstract class CamelRendererMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    @SuppressWarnings("unchecked")
    private void camelchest$addChestLayer(
        EntityRendererProvider.Context context,
        ModelLayerLocation modelLayer,
        CallbackInfo callback
    ) {
        RenderLayerParent<Camel, CamelModel<Camel>> parent =
            (RenderLayerParent<Camel, CamelModel<Camel>>) (Object) this;
        ((LivingEntityRendererAccessor) this).camelchest$addLayer(new CamelChestLayer(parent, context));
    }
}
