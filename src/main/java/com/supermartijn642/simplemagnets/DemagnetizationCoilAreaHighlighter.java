package com.supermartijn642.simplemagnets;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
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

    public static final RenderType RENDER_TYPE;

    static{
        RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(RenderTypeExtension.getShaderState()).setTransparencyState(RenderTypeExtension.getTranslucentTransparency()).setCullState(RenderTypeExtension.getCullState()).createCompositeState(true);
        RENDER_TYPE = RenderType.create("demagnetization_coil_highlight", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true, state);
    }

    @SubscribeEvent
    public static void onDrawHighlight(DrawSelectionEvent.HighlightBlock e){
        if(!SMConfig.showDemagnetizationArea.get())
            return;

        Level world = Minecraft.getInstance().level;
        BlockPos pos = e.getTarget().getBlockPos();
        BlockEntity tile = world.getBlockEntity(pos);
        if(tile instanceof DemagnetizationCoilTile){
            PoseStack matrixStack = e.getMatrix();
            matrixStack.pushPose();
            Vec3 playerPos = e.getInfo().getPosition();
            matrixStack.translate(-playerPos.x, -playerPos.y, -playerPos.z);

            drawBoundingBox(matrixStack, e.getBuffers(), pos, (DemagnetizationCoilTile)tile);

            matrixStack.popPose();
        }
    }

    private static void drawBoundingBox(PoseStack matrixStack, MultiBufferSource buffer, BlockPos pos, DemagnetizationCoilTile tile){
//        RenderSystem.enableBlend();
//        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//        RenderSystem.enableDepthTest();
//        RenderSystem.depthFunc(515);
//        RenderSystem.depthMask(true);

        VertexConsumer builder = buffer.getBuffer(RENDER_TYPE);
        float red = Math.abs(pos.getX() % 255) / 255f, green = Math.abs(pos.getY() % 255) / 255f, blue = Math.abs(pos.getZ() % 255) / 255f;
        renderSides(matrixStack, builder, tile.getArea(), red, green, blue, 0.2F);

//        RenderSystem.disableBlend();
    }

    private static void renderSides(PoseStack stack, VertexConsumer builder, AABB pos, float red, float green, float blue, float alpha){
        Matrix4f matrix = stack.last().pose();

        float minX = (float)pos.minX, maxX = (float)pos.maxX;
        float minY = (float)pos.minY, maxY = (float)pos.maxY;
        float minZ = (float)pos.minZ, maxZ = (float)pos.maxZ;

        builder.vertex(matrix, minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();


        builder.vertex(matrix, minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();


        builder.vertex(matrix, minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
    }

    private static class RenderTypeExtension extends RenderType {

        // Because stupid protected inner classes
        public RenderTypeExtension(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_){
            super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
        }

        public static TransparencyStateShard getTranslucentTransparency(){
            return RenderStateShard.TRANSLUCENT_TRANSPARENCY;
        }

        public static ShaderStateShard getShaderState(){
            return RenderStateShard.POSITION_COLOR_SHADER;
        }

        public static CullStateShard getCullState(){
            return RenderStateShard.NO_CULL;
        }
    }
}
