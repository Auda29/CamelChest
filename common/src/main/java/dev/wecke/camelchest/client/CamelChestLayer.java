package dev.wecke.camelchest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.animal.camel.CamelModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.CamelRenderState;
import net.minecraft.client.renderer.special.ChestSpecialRenderer;

public final class CamelChestLayer extends RenderLayer<CamelRenderState, CamelModel> {
    private static final float CHEST_SCALE = 0.58F;
    private static final float SIDE_OFFSET = 0.64F;
    private static final float HEIGHT_OFFSET = -0.36F;
    private static final float LENGTH_OFFSET = -0.48F;

    private final ChestSpecialRenderer chestRenderer;

    public CamelChestLayer(
        RenderLayerParent<CamelRenderState, CamelModel> parent,
        EntityRendererProvider.Context context
    ) {
        super(parent);
        chestRenderer = new ChestSpecialRenderer(
            context.getSprites(),
            new ChestModel(context.bakeLayer(ChestRenderer.LAYERS.single())),
            Sheets.CHEST_REGULAR.single(),
            0.0F
        );
    }

    @Override
    public void submit(
        PoseStack poseStack,
        SubmitNodeCollector collector,
        int packedLight,
        CamelRenderState state,
        float bodyRotation,
        float partialTick
    ) {
        if (state.isBaby || !((CamelChestRenderState) state).camelchest$hasChest()) {
            return;
        }

        poseStack.pushPose();
        ModelPart body = getParentModel().root().getChild("body");
        body.translateAndRotate(poseStack);

        submitChest(poseStack, collector, packedLight, state, -SIDE_OFFSET, 90.0F);
        submitChest(poseStack, collector, packedLight, state, SIDE_OFFSET, -90.0F);
        poseStack.popPose();
    }

    private void submitChest(
        PoseStack poseStack,
        SubmitNodeCollector collector,
        int packedLight,
        CamelRenderState state,
        float sideOffset,
        float rotation
    ) {
        poseStack.pushPose();
        poseStack.translate(sideOffset, HEIGHT_OFFSET, LENGTH_OFFSET);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.scale(CHEST_SCALE, CHEST_SCALE, CHEST_SCALE);
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        chestRenderer.submit(
            poseStack,
            collector,
            packedLight,
            LivingEntityRenderer.getOverlayCoords(state, 0.0F),
            false,
            0
        );
        poseStack.popPose();
    }
}
