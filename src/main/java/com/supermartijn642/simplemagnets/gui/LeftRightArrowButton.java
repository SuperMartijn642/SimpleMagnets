package com.supermartijn642.simplemagnets.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class LeftRightArrowButton extends GuiButton implements Pressable {

    private final ResourceLocation BUTTONS = new ResourceLocation("simplemagnets", "textures/left_right_arrow_buttons.png");

    private final boolean left;
    private final Runnable onPress;

    public LeftRightArrowButton(int buttonId, int x, int y, boolean left, Runnable onPress){
        super(buttonId, x, y, 11, 17, "");
        this.left = left;
        this.onPress = onPress;
    }

    @Override
    public void onPress(){
        this.onPress.run();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks){
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        Minecraft.getMinecraft().getTextureManager().bindTexture(BUTTONS);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        drawTexture(this.x, this.y, this.left ? 11 : 0, (this.enabled ? this.hovered ? 1 : 0 : 2) * 17);
    }

    private static void drawTexture(int x, int y, int textureX, int textureY){
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + 17, 0).tex(textureX / 22f, (textureY + 17) / 51f).endVertex();
        bufferbuilder.pos(x + 11, y + 17, 0).tex((textureX + 11) / 22f, (textureY + 17) / 51f).endVertex();
        bufferbuilder.pos(x + 11, y, 0).tex((textureX + 11) / 22f, textureY / 51f).endVertex();
        bufferbuilder.pos(x, y, 0).tex(textureX / 22f, textureY / 51f).endVertex();
        tessellator.draw();
    }
}
