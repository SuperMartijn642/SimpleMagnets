package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created 2/21/2021 by SuperMartijn642
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class DemagnetizationCoilAreaHighlighter {

    @SubscribeEvent
    public static void onDrawHighlight(DrawBlockHighlightEvent e){
        if(!SMConfig.showDemagnetizationArea.get())
            return;

        BlockPos pos = e.getTarget().getBlockPos();
        // Jup, pos can be null here even though its a non null return type
        if(pos != null){
            TileEntity tile = ClientUtils.getWorld().getTileEntity(pos);
            if(tile instanceof DemagnetizationCoilTile){
                GlStateManager.pushMatrix();
                Vec3d playerPos = RenderUtils.getCameraPosition();
                GlStateManager.translate(-playerPos.x, -playerPos.y, -playerPos.z);

                AxisAlignedBB area = ((DemagnetizationCoilTile)tile).getArea();
                float red = Math.abs(pos.getX() % 255) / 255f, green = Math.abs(pos.getY() % 255) / 255f, blue = Math.abs(pos.getZ() % 255) / 255f;
                RenderUtils.renderBox(area, red, green, blue, 0.3f);
                RenderUtils.renderBoxSides(area, red, green, blue, 0.2f);
                RenderUtils.resetState();

                GlStateManager.popMatrix();
            }
        }
    }
}
