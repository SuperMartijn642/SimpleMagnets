package com.supermartijn642.simplemagnets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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

        World world = Minecraft.getMinecraft().world;
        BlockPos pos = e.getTarget().getBlockPos();
        // Jup, pos can be null here even though its a non null return type
        if(pos != null){
            TileEntity tile = world.getTileEntity(pos);
            if(tile instanceof DemagnetizationCoilTile){
                EntityPlayer player = e.getPlayer();
                float partialTicks = e.getPartialTicks();
                double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
                double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
                double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
                drawBoundingBox(new Vec3d(x, y, z), pos, (DemagnetizationCoilTile)tile);
            }
        }
    }

    private static void drawBoundingBox(Vec3d viewPos, BlockPos pos, DemagnetizationCoilTile tile){
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.matrixMode(5889);
        GlStateManager.disableTexture2D();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(false);

        float red = Math.abs(pos.getX() % 255) / 255f, green = Math.abs(pos.getY() % 255) / 255f, blue = Math.abs(pos.getZ() % 255) / 255f;
        renderSides(viewPos, tile.getArea(), red, green, blue, 0.2F);

        GlStateManager.matrixMode(5888);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    private static void renderSides(Vec3d viewPos, AxisAlignedBB pos, float red, float green, float blue, float alpha){
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);

        float minX = (float)(pos.minX - viewPos.x), maxX = (float)(pos.maxX - viewPos.x);
        float minY = (float)(pos.minY - viewPos.y), maxY = (float)(pos.maxY - viewPos.y);
        float minZ = (float)(pos.minZ - viewPos.z), maxZ = (float)(pos.maxZ - viewPos.z);

        builder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();

        builder.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();


        builder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();

        builder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();


        builder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();

        builder.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();

        tessellator.draw();
    }
}
