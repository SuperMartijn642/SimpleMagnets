package com.supermartijn642.simplemagnets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created 2/21/2021 by SuperMartijn642
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DemagnetizationCoilAreaHighlighter {

    public static final RenderType RENDER_TYPE;

    static{
        RenderType.State state = RenderType.State.getBuilder().transparency(new RenderState.TransparencyState("translucent_transparency", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.enableAlphaTest();
        }, () -> {
            RenderSystem.disableBlend();
            RenderSystem.disableAlphaTest();
        })).build(true);
        RENDER_TYPE = RenderType.makeType("demagnetization_coil_highlight", DefaultVertexFormats.POSITION_COLOR, 7, 256, false, true, state);
    }

    @SubscribeEvent
    public static void onDrawHighlight(DrawHighlightEvent.HighlightBlock e){
        if(!SMConfig.showDemagnetizationArea.get())
            return;

        World world = Minecraft.getInstance().world;
        BlockPos pos = e.getTarget().getPos();
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof DemagnetizationCoilTile){
            MatrixStack matrixStack = e.getMatrix();
            matrixStack.push();
            Vector3d playerPos = e.getInfo().getProjectedView();
            matrixStack.translate(-playerPos.x, -playerPos.y, -playerPos.z);

            drawBoundingBox(matrixStack, e.getBuffers(), pos, (DemagnetizationCoilTile)tile);

            matrixStack.pop();
        }
    }

    private static void drawBoundingBox(MatrixStack matrixStack, IRenderTypeBuffer buffer, BlockPos pos, DemagnetizationCoilTile tile){
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.disableTexture();
        RenderHelper.disableStandardItemLighting();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(515);
        RenderSystem.depthMask(true);

        IVertexBuilder builder = buffer.getBuffer(RENDER_TYPE);
        float red = Math.abs(pos.getX() % 255) / 255f, green = Math.abs(pos.getY() % 255) / 255f, blue = Math.abs(pos.getZ() % 255) / 255f;
        renderSides(matrixStack, builder, tile.getArea(), red, green, blue, 0.2F);

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    private static void renderSides(MatrixStack stack, IVertexBuilder builder, AxisAlignedBB pos, float red, float green, float blue, float alpha){
        Matrix4f matrix = stack.getLast().getMatrix();

        float minX = (float)pos.minX, maxX = (float)pos.maxX;
        float minY = (float)pos.minY, maxY = (float)pos.maxY;
        float minZ = (float)pos.minZ, maxZ = (float)pos.maxZ;

        builder.pos(matrix, minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();

        builder.pos(matrix, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();


        builder.pos(matrix, minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();

        builder.pos(matrix, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();


        builder.pos(matrix, minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();

        builder.pos(matrix, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
    }
}
