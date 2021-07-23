package com.supermartijn642.simplemagnets.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.TileEntityBaseContainerScreen;
import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public abstract class BaseDemagnetizationCoilContainerScreen<T extends BaseDemagnetizationCoilContainer> extends TileEntityBaseContainerScreen<DemagnetizationCoilTile,T> {

    public BaseDemagnetizationCoilContainerScreen(T container, String title){
        super(container, new TranslatableComponent(title));
    }

    @Override
    protected int sizeX(DemagnetizationCoilTile demagnetizationCoilTile){
        return this.menu.width;
    }

    @Override
    protected int sizeY(DemagnetizationCoilTile demagnetizationCoilTile){
        return this.menu.height;
    }

    @Override
    protected void containerTick(@Nonnull DemagnetizationCoilTile blockEntity){
        DemagnetizationCoilTile tile = this.getObjectOrClose();
        if(tile == null)
            return;

        super.tick();
        this.tick(tile);
    }

    protected abstract void tick(DemagnetizationCoilTile tile);

    protected abstract String getBackground();

    @Override
    protected void renderBackground(PoseStack matrixStack, int mouseX, int mouseY, DemagnetizationCoilTile object){
        ScreenUtils.bindTexture(new ResourceLocation("simplemagnets", "textures/" + this.getBackground()));
        ScreenUtils.drawTexture(matrixStack, 0, 0, this.sizeX(), this.sizeY());
    }
}
