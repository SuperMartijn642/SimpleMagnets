package com.supermartijn642.simplemagnets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.render.RenderUtils;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
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
        WorldRenderEvents.AFTER_BLOCK_OUTLINE_EXTRACTION.register((context, result) -> {
            AreaHighlightState state = context.worldState().getData(HIGHLIGHT_DATA);
            if(state == null){
                state = new AreaHighlightState();
                context.worldState().setData(HIGHLIGHT_DATA, state);
            }
            state.shouldRender = false;
            if(!SMConfig.showDemagnetizationArea.get())
                return;

            if(result instanceof BlockHitResult){
                BlockPos pos = ((BlockHitResult)result).getBlockPos();
                BlockEntity entity = context.world().getBlockEntity(pos);
                if(entity instanceof DemagnetizationCoilBlockEntity){
                    state.shouldRender = true;
                    state.pos = pos;
                    state.area = ((DemagnetizationCoilBlockEntity)entity).getArea();
                }
            }
        });

        // Render highlight
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, outlineRenderState) -> {
            AreaHighlightState state = context.worldState().getData(HIGHLIGHT_DATA);
            if(state == null || !state.shouldRender)
                return true;

            POSE_STACK.pushPose();
            Vec3 playerPos = context.worldState().cameraRenderState.pos;
            POSE_STACK.translate(-playerPos.x, -playerPos.y, -playerPos.z);

            Random random = new Random(state.pos.hashCode());
            float red = random.nextFloat();
            float green = random.nextFloat();
            float blue = random.nextFloat();
            float alpha = 0.3f;

            RenderUtils.renderBox(POSE_STACK, state.area, red, green, blue, alpha, true);
            RenderUtils.renderBoxSides(POSE_STACK, state.area, red, green, blue, alpha, true);

            POSE_STACK.popPose();
            return true;
        });
    }

    private static class AreaHighlightState {
        boolean shouldRender;
        BlockPos pos;
        AABB area;
    }
}
