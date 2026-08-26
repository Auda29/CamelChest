package dev.wecke.camelchest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.wecke.camelchest.api.CamelChestAccess;
import net.minecraft.client.model.CamelModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.animal.camel.Camel;

public final class CamelChestLayer extends RenderLayer<Camel, CamelModel<Camel>> {
    private static final float CHEST_SCALE = 0.58F;
    private static final float SIDE_OFFSET = 0.64F;
    private static final float HEIGHT_OFFSET = -0.36F;
    private static final float LENGTH_OFFSET = -0.48F;

    private final ModelPart chestModel;

    public CamelChestLayer(
        RenderLayerParent<Camel, CamelModel<Camel>> parent,
        EntityRendererProvider.Context context
    ) {
        super(parent);
        chestModel = context.bakeLayer(ModelLayers.CHEST);
    }

    @Override
    public void render(
        PoseStack poseStack,
        MultiBufferSource buffers,
        int packedLight,
        Camel camel,
        float limbSwing,
        float limbSwingAmount,
        float partialTick,
        float ageInTicks,
        float netHeadYaw,
        float headPitch
    ) {
        if (camel.isBaby() || !((CamelChestAccess) camel).camelchest$hasChest()) {
            return;
        }

        poseStack.pushPose();
        getParentModel().root().getChild("body").translateAndRotate(poseStack);
        VertexConsumer vertices = Sheets.CHEST_LOCATION.buffer(buffers, RenderType::entityCutout);

        renderChest(poseStack, vertices, packedLight, camel, -SIDE_OFFSET, 90.0F);
        renderChest(poseStack, vertices, packedLight, camel, SIDE_OFFSET, -90.0F);
        poseStack.popPose();
    }

    private void renderChest(
        PoseStack poseStack,
        VertexConsumer vertices,
        int packedLight,
        Camel camel,
        float sideOffset,
        float rotation
    ) {
        poseStack.pushPose();
        poseStack.translate(sideOffset, HEIGHT_OFFSET, LENGTH_OFFSET);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.scale(CHEST_SCALE, CHEST_SCALE, CHEST_SCALE);
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        chestModel.render(
            poseStack,
            vertices,
            packedLight,
            LivingEntityRenderer.getOverlayCoords(camel, 0.0F)
        );
        poseStack.popPose();
    }
}
