package com.supermartijn642.simplemagnets.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public abstract class BaseDemagnetizationCoilContainerScreen<T extends BaseDemagnetizationCoilContainer> extends ContainerScreen<T> {

    public BaseDemagnetizationCoilContainerScreen(T container, String title){
        super(container, container.player.inventory, new TranslationTextComponent(title));
        this.xSize = container.width;
        this.ySize = container.height;
    }

    @Override
    protected void init(){
        super.init();

        DemagnetizationCoilTile tile = this.container.getTileOrClose();
        if(tile != null)
            this.addButtons(tile);
    }

    protected abstract void addButtons(DemagnetizationCoilTile tile);

    @Override
    public void render(int mouseX, int mouseY, float partialTicks){
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

        DemagnetizationCoilTile tile = this.container.getTileOrClose();
        if(tile != null)
            this.drawToolTips(tile, mouseX, mouseY);
    }

    protected abstract void drawToolTips(DemagnetizationCoilTile tile, int mouseX, int mouseY);

    @Override
    public void tick(){
        DemagnetizationCoilTile tile = this.container.getTileOrClose();
        if(tile == null)
            return;

        super.tick();
        this.tick(tile);
    }

    protected abstract void tick(DemagnetizationCoilTile tile);

    protected abstract String getBackground();

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("simplemagnets", "textures/" + this.getBackground()));
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        DemagnetizationCoilTile tile = this.container.getTileOrClose();
        if(tile != null)
            this.drawText(tile);
    }

    protected abstract void drawText(DemagnetizationCoilTile tile);

    public void drawCenteredString(ITextComponent text, float x, float y){
        String s = text.getFormattedText();
        this.font.drawString(s, this.guiLeft + x - this.font.getStringWidth(s) / 2f, this.guiTop + y, 4210752);
    }

    public void drawString(ITextComponent text, float x, float y){
        String s = text.getFormattedText();
        this.font.drawString(s, this.guiLeft + x, this.guiTop + y, 4210752);
    }

    public void renderTooltip(boolean translate, String string, int x, int y){
        super.renderTooltip(translate ? new TranslationTextComponent(string).getFormattedText() : string, x, y);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_230451_2_, int p_230451_3_){
        // stop default text drawing
    }
}
