package com.supermartijn642.simplemagnets.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class ArrowButton extends AbstractButton {

    private final ResourceLocation BUTTONS = new ResourceLocation("simplemagnets", "textures/arrow_buttons.png");

    private final boolean left;
    private final Runnable onPress;

    public ArrowButton(int x, int y, boolean left, Runnable onPress){
        super(x, y, 11, 17, new StringTextComponent(""));
        this.left = left;
        this.onPress = onPress;
    }

    @Override
    public void onPress(){
        this.onPress.run();
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks){
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(BUTTONS);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        drawTexture(this.x, this.y, this.left ? 11 : 0, (this.active ? this.isHovered ? 1 : 0 : 2) * 17);
        this.renderBg(matrixStack, minecraft, mouseX, mouseY);
    }

    private static void drawTexture(int x, int y, int textureX, int textureY){
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + 17, 0).tex(textureX / 22f, (textureY + 17) / 51f).endVertex();
        bufferbuilder.pos(x + 11, y + 17, 0).tex((textureX + 11) / 22f, (textureY + 17) / 51f).endVertex();
        bufferbuilder.pos(x + 11, y, 0).tex((textureX + 11) / 22f, textureY / 51f).endVertex();
        bufferbuilder.pos(x, y, 0).tex(textureX / 22f, textureY / 51f).endVertex();
        bufferbuilder.finishDrawing();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.draw(bufferbuilder);
    }
}
