package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import com.supermartijn642.core.util.Holder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Consumer;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class UpDownArrowButton extends AbstractButtonWidget {

    private static final ResourceLocation BUTTONS = new ResourceLocation("simplemagnets", "textures/up_down_arrow_buttons.png");

    private final boolean down;
    public boolean active = true;

    public UpDownArrowButton(int x, int y, boolean down, Runnable onPress){
        super(x, y, 17, 11, onPress);
        this.down = down;
    }

    @Override
    public void render(int mouseX, int mouseY){
        ScreenUtils.bindTexture(BUTTONS);
        ScreenUtils.drawTexture(this.x, this.y, this.width, this.height, this.down ? 0.5f : 0, (this.active ? this.isFocused() ? 1 : 0 : 2) / 3f, 0.5f, 1 / 3f);
    }

    @Override
    protected void getTooltips(Consumer<ITextComponent> tooltips){
        tooltips.accept(TextComponents.translation(this.down ? "simplemagnets.gui.demagnetization_coil.range.decrease" : "simplemagnets.gui.demagnetization_coil.range.increase").get());
    }

    @Override
    public ITextComponent getNarrationMessage(){
        Holder<ITextComponent> message = new Holder<>();
        this.getTooltips(message::set);
        return message.get();
    }
}
