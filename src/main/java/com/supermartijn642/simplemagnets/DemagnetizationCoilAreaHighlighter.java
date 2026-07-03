package com.supermartijn642.simplemagnets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.RenderUtils;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

/**
 * Created 2/21/2021 by SuperMartijn642
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class DemagnetizationCoilAreaHighlighter {

    private static final PoseStack POSE_STACK = new PoseStack();

    @SubscribeEvent
    private static void onBlockHighlightExtract(RenderHighlightEvent.Block event){
        if(!SMConfig.showDemagnetizationArea.get())
            return;

        BlockPos pos = event.getTarget().getBlockPos();
        Level level = ClientUtils.getWorld();
        BlockEntity entity = level.getBlockEntity(pos);
        if(entity instanceof DemagnetizationCoilBlockEntity){
            AreaHighlightState state = new AreaHighlightState();
            state.shouldRender = true;
            state.pos = pos;
            state.area = ((DemagnetizationCoilBlockEntity)entity).getArea();
            BlockState blockState = level.getBlockState(pos);
            //noinspection deprecation
            BlockOutlineRenderState outlineRenderState = new BlockOutlineRenderState(
                pos,
                ClientUtils.getMinecraft().getModelManager().getBlockStateModelSet().get(blockState).hasMaterialFlag(BakedQuad.FLAG_TRANSLUCENT),
                ClientUtils.getMinecraft().options.highContrastBlockOutline().get(),
                blockState.getShape(level, pos, CollisionContext.of(event.getCamera().entity()))
            );
            LevelRenderer levelRenderer = event.getLevelRenderer();
            event.setCustomRenderer((source, stack, translucent, levelRenderState) -> onRenderBlockOutline(outlineRenderState, source, stack, translucent, levelRenderState, levelRenderer, state));
        }
    }

    private static boolean onRenderBlockOutline(BlockOutlineRenderState outlineRenderState, MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, boolean translucentPass, LevelRenderState levelRenderState, LevelRenderer levelRenderer, AreaHighlightState state){
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

        // Render original outline
        BlockOutlineRenderState temp = levelRenderState.blockOutlineRenderState;
        levelRenderState.blockOutlineRenderState = outlineRenderState;
        levelRenderer.renderBlockOutline(bufferSource, poseStack, translucentPass, levelRenderState);
        levelRenderState.blockOutlineRenderState = temp;
        return false;
    }

    private static class AreaHighlightState {
        boolean shouldRender;
        BlockPos pos;
        AABB area;
    }
}
