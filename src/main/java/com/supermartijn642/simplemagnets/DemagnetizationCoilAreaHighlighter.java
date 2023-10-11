package com.supermartijn642.simplemagnets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.RenderUtils;
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

    public static void onDrawHighlight(PoseStack poseStack, BlockHitResult target){
        if(!SMConfig.showDemagnetizationArea.get())
            return;

        BlockPos pos = target.getBlockPos();
        BlockEntity entity = ClientUtils.getWorld().getBlockEntity(pos);
        if(entity instanceof DemagnetizationCoilBlockEntity){
            poseStack.pushPose();
            Vec3 playerPos = RenderUtils.getCameraPosition();
            poseStack.translate(-playerPos.x, -playerPos.y, -playerPos.z);

            AABB area = ((DemagnetizationCoilBlockEntity)entity).getArea();

            Random random = new Random(entity.getBlockPos().hashCode());
            float red = random.nextFloat();
            float green = random.nextFloat();
            float blue = random.nextFloat();
            float alpha = 0.3f;

            RenderUtils.renderBox(poseStack, area, red, green, blue, alpha, true);
            RenderUtils.renderBoxSides(poseStack, area, red, green, blue, alpha, true);

            poseStack.popPose();
        }
    }
}
