package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.BlockEntityBaseContainerWidget;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.simplemagnets.DemagnetizationCoilBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public abstract class BaseDemagnetizationCoilContainerScreen<T extends BaseDemagnetizationCoilContainer> extends BlockEntityBaseContainerWidget<DemagnetizationCoilBlockEntity,T> {

    private final ResourceLocation background = ResourceLocation.fromNamespaceAndPath("simplemagnets", "textures/" + this.getBackground());

    public BaseDemagnetizationCoilContainerScreen(){
        super(0, 0, 0, 0, null, null);
    }

    @Override
    protected Component getNarrationMessage(DemagnetizationCoilBlockEntity object){
        return TextComponents.block(object.getBlockState().getBlock()).get();
    }

    @Override
    protected int width(DemagnetizationCoilBlockEntity entity){
        return this.container.width;
    }

    @Override
    protected int height(DemagnetizationCoilBlockEntity entity){
        return this.container.height;
    }

    @Override
    protected DemagnetizationCoilBlockEntity getObject(DemagnetizationCoilBlockEntity oldObject){
        return this.container.getObject(oldObject);
    }

    protected abstract String getBackground();

    @Override
    protected void renderBackground(WidgetRenderContext context, int mouseX, int mouseY, DemagnetizationCoilBlockEntity object){
        ScreenUtils.drawTexture(this.background, context.poseStack(), 0, 0, this.width(), this.height());
        super.renderBackground(context, mouseX, mouseY, object);
    }
}
