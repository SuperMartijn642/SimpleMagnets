package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.AbstractButtonWidget;
import com.supermartijn642.core.gui.widget.IHoverTextWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class UpDownArrowButton extends AbstractButtonWidget implements IHoverTextWidget {

    private final ResourceLocation BUTTONS = new ResourceLocation("simplemagnets", "textures/up_down_arrow_buttons.png");

    private final boolean down;

    public UpDownArrowButton(int x, int y, boolean down, Runnable onPress){
        super(x, y, 17, 11, onPress);
        this.down = down;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks){
        ScreenUtils.bindTexture(BUTTONS);
        ScreenUtils.drawTexture(this.x, this.y, this.width, this.height, this.down ? 0.5f : 0, (this.active ? this.hovered ? 1 : 0 : 2) / 3f, 0.5f, 1 / 3f);
    }

    @Override
    public ITextComponent getHoverText(){
        return new TextComponentTranslation(this.down ? "gui.simplemagnets.demagnetization_coil.range.decrease" : "gui.simplemagnets.demagnetization_coil.range.increase");
    }

    @Override
    protected ITextComponent getNarrationMessage(){
        return this.getHoverText();
    }
}
