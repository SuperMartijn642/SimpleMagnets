package com.supermartijn642.simplemagnets.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.TileEntityBaseContainerScreen;
import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public abstract class BaseDemagnetizationCoilContainerScreen<T extends BaseDemagnetizationCoilContainer> extends TileEntityBaseContainerScreen<DemagnetizationCoilTile,T> {

    public BaseDemagnetizationCoilContainerScreen(T container, String title){
        super(container, new TranslationTextComponent(title));
    }

    @Override
    protected int sizeX(DemagnetizationCoilTile demagnetizationCoilTile){
        return this.container.width;
    }

    @Override
    protected int sizeY(DemagnetizationCoilTile demagnetizationCoilTile){
        return this.container.height;
    }

    @Override
    public void tick(){
        DemagnetizationCoilTile tile = this.getObjectOrClose();
        if(tile == null)
            return;

        super.tick();
        this.tick(tile);
    }

    protected abstract void tick(DemagnetizationCoilTile tile);

    protected abstract String getBackground();

    @Override
    protected void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, DemagnetizationCoilTile object){
        ScreenUtils.bindTexture(new ResourceLocation("simplemagnets", "textures/" + this.getBackground()));
        ScreenUtils.drawTexture(matrixStack, 0, 0, this.sizeX(), this.sizeY());
    }
}
