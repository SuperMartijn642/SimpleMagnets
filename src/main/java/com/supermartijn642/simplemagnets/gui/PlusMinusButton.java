package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.AbstractButtonWidget;
import com.supermartijn642.core.gui.widget.IHoverTextWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class PlusMinusButton extends AbstractButtonWidget implements IHoverTextWidget {

    private static final ResourceLocation BUTTONS = new ResourceLocation("simplemagnets", "textures/plus_minus_buttons.png");

    private final boolean left;
    private final String translationKey;

    public PlusMinusButton(int x, int y, boolean left, String translationKey, Runnable onPress){
        super(x, y, 17, 17, onPress);
        this.left = left;
        this.translationKey = translationKey;
    }

    @Override
    protected ITextComponent getNarrationMessage(){
        return this.getHoverText();
    }

    @Override
    public ITextComponent getHoverText(){
        return TextComponents.translation(this.translationKey).get();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks){
        ScreenUtils.bindTexture(BUTTONS);
        ScreenUtils.drawTexture(this.x, this.y, this.width, this.height, this.left ? 0.5f : 0, (this.active ? this.hovered ? 1 : 0 : 2) / 3f, 0.5f, 1 / 3f);
    }
}
