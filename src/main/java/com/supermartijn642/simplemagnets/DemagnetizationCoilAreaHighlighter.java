package com.supermartijn642.simplemagnets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ExtractBlockOutlineRenderStateEvent;

import java.util.Random;

/**
 * Created 2/21/2021 by SuperMartijn642
 */
@EventBusSubscriber(Dist.CLIENT)
public class DemagnetizationCoilAreaHighlighter {

    private static final ContextKey<AreaHighlightState> HIGHLIGHT_DATA = new ContextKey<>(Identifier.fromNamespaceAndPath("simplemagnets", "demagnetization_coil_area_highlight"));
    private static final PoseStack POSE_STACK = new PoseStack();

    @SubscribeEvent
    private static void onExtractBlockOutline(ExtractBlockOutlineRenderStateEvent event){
        AreaHighlightState state = event.getLevelRenderState().getRenderData(HIGHLIGHT_DATA);
        if(state == null){
            state = new AreaHighlightState();
            event.getLevelRenderState().setRenderData(HIGHLIGHT_DATA, state);
        }
        state.shouldRender = false;
        if(!SMConfig.showDemagnetizationArea.get())
            return;

        BlockPos pos = event.getHitResult().getBlockPos();
        BlockEntity entity = event.getLevel().getBlockEntity(pos);
        if(entity instanceof DemagnetizationCoilBlockEntity){
            state.shouldRender = true;
            state.pos = pos;
            state.area = ((DemagnetizationCoilBlockEntity)entity).getArea();
            event.addCustomRenderer(DemagnetizationCoilAreaHighlighter::onRenderBlockOutline);
        }
    }

    private static boolean onRenderBlockOutline(BlockOutlineRenderState outlineRenderState, MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, boolean translucentPass, LevelRenderState levelRenderState){
        AreaHighlightState state = levelRenderState.getRenderData(HIGHLIGHT_DATA);
        if(state == null || !state.shouldRender)
            return false;

        POSE_STACK.pushPose();
        Vec3 playerPos = levelRenderState.cameraRenderState.pos;
        POSE_STACK.translate(-playerPos.x, -playerPos.y, -playerPos.z);

        Random random = new Random(state.pos.hashCode());
        float red = random.nextFloat();
        float green = random.nextFloat();
        float blue = random.nextFloat();
        float alpha = 0.3f;

        RenderUtils.renderBox(POSE_STACK, state.area, red, green, blue, alpha, true);
        RenderUtils.renderBoxSides(POSE_STACK, state.area, red, green, blue, alpha, true);

        POSE_STACK.popPose();
        return false;
    }

    private static class AreaHighlightState {
        boolean shouldRender;
        BlockPos pos;
        AABB area;
    }
}
