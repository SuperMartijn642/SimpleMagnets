package com.supermartijn642.simplemagnets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.block.BlockShape;
import com.supermartijn642.core.render.CustomBlockEntityRenderer;
import com.supermartijn642.core.render.RenderUtils;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

import java.util.Random;

/**
 * Created 6/9/2021 by SuperMartijn642
 */
public class DemagnetizationCoilBlockEntityRenderer implements CustomBlockEntityRenderer<DemagnetizationCoilBlockEntity,DemagnetizationCoilBlockEntityRenderer.State> {

    @Override
    public State createStateHolder(){
        return new State();
    }

    @Override
    public void updateState(State state, DemagnetizationCoilBlockEntity entity, UpdateContext context){
        state.showRange = entity.getShowRange();
        if(state.showRange){
            state.pos = entity.getBlockPos();
            state.area = entity.getArea();
        }
    }

    @Override
    public void submit(SubmitNodeCollector output, State state, RenderContext context){
        if(state.showRange){
            PoseStack poseStack = context.poseStack();
            poseStack.pushPose();
            poseStack.translate(-state.pos.getX(), -state.pos.getY(), -state.pos.getZ());

            BlockShape area = BlockShape.create(state.area.inflate(0.05f));

            Random random = new Random(state.pos.hashCode());
            float red = random.nextFloat();
            float green = random.nextFloat();
            float blue = random.nextFloat();
            float alpha = 0.3f;

            RenderUtils.submitShape(output, poseStack, area, red, green, blue, 1, true);
            RenderUtils.submitShapeSides(output, poseStack, area, red, green, blue, alpha, true);

            poseStack.popPose();
        }
    }

    public static class State {
        boolean showRange;
        BlockPos pos;
        AABB area;
    }
}
