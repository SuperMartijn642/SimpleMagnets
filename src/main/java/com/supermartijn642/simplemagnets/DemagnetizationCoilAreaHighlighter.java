package com.supermartijn642.simplemagnets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.block.BlockShape;
import com.supermartijn642.core.render.RenderUtils;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

/**
 * Created 2/21/2021 by SuperMartijn642
 */
public class DemagnetizationCoilAreaHighlighter {

    private static final RenderStateDataKey<AreaHighlightState> HIGHLIGHT_DATA = RenderStateDataKey.create(() -> "simplemagnets:demagnetization_coil_area_highlight");
    private static final PoseStack POSE_STACK = new PoseStack();

    public static void registerEventListeners(){
        // Extract state
        LevelRenderEvents.AFTER_BLOCK_OUTLINE_EXTRACTION.register((context, result) -> {
            AreaHighlightState state = context.levelState().getData(HIGHLIGHT_DATA);
            if(state == null){
                state = new AreaHighlightState();
                context.levelState().setData(HIGHLIGHT_DATA, state);
            }
            state.shouldRender = false;
            if(!SMConfig.showDemagnetizationArea.get())
                return;

            if(result instanceof BlockHitResult){
                BlockPos pos = ((BlockHitResult)result).getBlockPos();
                BlockEntity entity = context.level().getBlockEntity(pos);
                if(entity instanceof DemagnetizationCoilBlockEntity){
                    state.shouldRender = true;
                    state.pos = pos;
                    state.area = BlockShape.create(((DemagnetizationCoilBlockEntity)entity).getArea());
                }
            }
        });

        // Render highlight
        LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, outlineRenderState) -> {
            AreaHighlightState state = context.levelState().getData(HIGHLIGHT_DATA);
            if(state == null || !state.shouldRender)
                return true;

            POSE_STACK.pushPose();
            Vec3 playerPos = context.levelState().cameraRenderState.pos;
            POSE_STACK.translate(-playerPos.x, -playerPos.y, -playerPos.z);

            Random random = new Random(state.pos.hashCode());
            float red = random.nextFloat();
            float green = random.nextFloat();
            float blue = random.nextFloat();
            float alpha = 0.3f;

            SubmitNodeCollector output = context.submitNodeCollector();
            RenderUtils.submitShape(output, POSE_STACK, state.area, red, green, blue, alpha, true);
            RenderUtils.submitShapeSides(output, POSE_STACK, state.area, red, green, blue, alpha, true);

            POSE_STACK.popPose();
            return true;
        });
    }

    private static class AreaHighlightState {
        boolean shouldRender;
        BlockPos pos;
        BlockShape area;
    }
}
