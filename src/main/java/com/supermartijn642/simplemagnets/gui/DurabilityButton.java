package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.AbstractButtonWidget;
import com.supermartijn642.core.gui.widget.IHoverTextWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class DurabilityButton extends AbstractButtonWidget implements IHoverTextWidget {

    private final ResourceLocation BUTTONS = new ResourceLocation("simplemagnets", "textures/durability_button.png");

    public boolean on = true;

    public DurabilityButton(int x, int y, Runnable onPress){
        super(x, y, 20, 20, onPress);
    }

    public void update(boolean on){
        this.on = on;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks){
        ScreenUtils.bindTexture(BUTTONS);
        ScreenUtils.drawTexture(this.x, this.y, this.width, this.height, this.on ? 0 : 0.5f, (this.active ? this.hovered ? 1 : 0 : 2) / 3f, 0.5f, 1 / 3f);
    }

    @Override
    public ITextComponent getHoverText(){
        return new TranslationTextComponent("gui.simplemagnets.demagnetization_coil.durability." + (this.on ? "on" : "off"));
    }

    @Override
    protected ITextComponent getNarrationMessage(){
        return this.getHoverText();
    }
}
