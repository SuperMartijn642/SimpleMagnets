package com.supermartijn642.simplemagnets.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
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
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks){
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrixStack, mouseX, mouseY);

        DemagnetizationCoilTile tile = this.container.getTileOrClose();
        if(tile != null)
            this.drawToolTips(matrixStack, tile, mouseX, mouseY);
    }

    protected abstract void drawToolTips(MatrixStack matrixStack, DemagnetizationCoilTile tile, int mouseX, int mouseY);

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
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY){
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("simplemagnets", "textures/" + this.getBackground()));
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        DemagnetizationCoilTile tile = this.container.getTileOrClose();
        if(tile != null)
            this.drawText(matrixStack, tile);
    }

    protected abstract void drawText(MatrixStack matrixStack, DemagnetizationCoilTile tile);

    public void drawCenteredString(MatrixStack matrixStack, ITextComponent text, float x, float y){
        this.font.func_238422_b_(matrixStack, text, this.guiLeft + x - this.font.func_238414_a_(text) / 2f, this.guiTop + y, 4210752);
    }

    public void drawString(MatrixStack matrixStack, ITextComponent text, float x, float y){
        this.font.func_238422_b_(matrixStack, text, this.guiLeft + x, this.guiTop + y, 4210752);
    }

    public void renderToolTip(MatrixStack matrixStack, boolean translate, String string, int x, int y){
        super.renderTooltip(matrixStack, translate ? new TranslationTextComponent(string) : new StringTextComponent(string), x, y);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_){
        // stop default text drawing
    }
}
