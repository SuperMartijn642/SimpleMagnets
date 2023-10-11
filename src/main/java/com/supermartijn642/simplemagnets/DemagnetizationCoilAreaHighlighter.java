package com.supermartijn642.simplemagnets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.RenderUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawSelectionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

/**
 * Created 2/21/2021 by SuperMartijn642
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DemagnetizationCoilAreaHighlighter {

    @SubscribeEvent
    public static void onDrawHighlight(DrawSelectionEvent.HighlightBlock e){
        if(!SMConfig.showDemagnetizationArea.get())
            return;

        BlockPos pos = e.getTarget().getBlockPos();
        BlockEntity entity = ClientUtils.getWorld().getBlockEntity(pos);
        if(entity instanceof DemagnetizationCoilBlockEntity){
            PoseStack poseStack = e.getMatrix();
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
