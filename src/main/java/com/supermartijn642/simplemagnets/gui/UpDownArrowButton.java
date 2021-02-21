package com.supermartijn642.simplemagnets.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class UpDownArrowButton extends AbstractButton {

    private final ResourceLocation BUTTONS = new ResourceLocation("simplemagnets", "textures/up_down_arrow_buttons.png");

    private final boolean down;
    private final Runnable onPress;

    public UpDownArrowButton(int x, int y, boolean down, Runnable onPress){
        super(x, y, 17, 11, "");
        this.down = down;
        this.onPress = onPress;
    }

    @Override
    public void onPress(){
        this.onPress.run();
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks){
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(BUTTONS);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        drawTexture(this.x, this.y, this.down ? 17 : 0, (this.active ? this.isHovered ? 1 : 0 : 2) * 11);
        this.renderBg(minecraft, mouseX, mouseY);
    }

    private static void drawTexture(int x, int y, int textureX, int textureY){
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + 11, 0).tex(textureX / 34f, (textureY + 11) / 33f).endVertex();
        bufferbuilder.pos(x + 17, y + 11, 0).tex((textureX + 17) / 34f, (textureY + 11) / 33f).endVertex();
        bufferbuilder.pos(x + 17, y, 0).tex((textureX + 17) / 34f, textureY / 33f).endVertex();
        bufferbuilder.pos(x, y, 0).tex(textureX / 34f, textureY / 33f).endVertex();
        tessellator.draw();
    }
}
