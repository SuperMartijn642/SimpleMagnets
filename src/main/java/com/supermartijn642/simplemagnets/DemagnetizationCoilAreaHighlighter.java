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
            float red = Math.abs(pos.getX() % 255) / 255f, green = Math.abs(pos.getY() % 255) / 255f, blue = Math.abs(pos.getZ() % 255) / 255f;
            RenderUtils.renderBox(poseStack, area, red, green, blue, 0.3f, true);
            RenderUtils.renderBoxSides(poseStack, area, red, green, blue, 0.2f, true);

            poseStack.popPose();
        }
    }
}
