package com.supermartijn642.simplemagnets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.RenderUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created 2/21/2021 by SuperMartijn642
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DemagnetizationCoilAreaHighlighter {

    @SubscribeEvent
    public static void onDrawHighlight(DrawHighlightEvent.HighlightBlock e){
        if(!SMConfig.showDemagnetizationArea.get())
            return;

        BlockPos pos = e.getTarget().getBlockPos();
        TileEntity tile = ClientUtils.getWorld().getBlockEntity(pos);
        if(tile instanceof DemagnetizationCoilTile){
            MatrixStack matrixStack = e.getMatrix();
            matrixStack.pushPose();
            Vector3d playerPos = e.getInfo().getPosition();
            matrixStack.translate(-playerPos.x, -playerPos.y, -playerPos.z);

            AxisAlignedBB area = ((DemagnetizationCoilTile)tile).getArea();
            float red = Math.abs(pos.getX() % 255) / 255f, green = Math.abs(pos.getY() % 255) / 255f, blue = Math.abs(pos.getZ() % 255) / 255f;
            RenderUtils.renderBox(matrixStack, area, red, green, blue, 0.3f);
            RenderUtils.renderBoxSides(matrixStack, area, red, green, blue, 0.2f);
            RenderUtils.resetState();

            matrixStack.popPose();
        }
    }
}
