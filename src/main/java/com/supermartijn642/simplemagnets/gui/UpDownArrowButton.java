package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.GuiGraphicsHelper;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import com.supermartijn642.core.util.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class UpDownArrowButton extends AbstractButtonWidget {

    public static final Identifier BUTTONS = Identifier.fromNamespaceAndPath("simplemagnets", "up_down_arrow_buttons");

    private final boolean down;
    public boolean active = true;

    public UpDownArrowButton(int x, int y, boolean down, Runnable onPress){
        super(x, y, 17, 11, onPress);
        this.down = down;
    }

    @Override
    public void render(WidgetRenderContext context, GuiGraphicsHelper graphics, int mouseX, int mouseY){
        graphics.submitSprite(BUTTONS, this.x, this.y, this.width, this.height, p -> p.uv(this.down ? 0.5f : 0, (this.active ? this.isFocused() ? 1 : 0 : 2) / 3f, 0.5f, 1 / 3f));
    }

    @Override
    protected void getTooltips(Consumer<Component> tooltips){
        tooltips.accept(TextComponents.translation(this.down ? "simplemagnets.gui.demagnetization_coil.range.decrease" : "simplemagnets.gui.demagnetization_coil.range.increase").get());
    }

    @Override
    public Component getNarrationMessage(){
        Holder<Component> message = new Holder<>();
        this.getTooltips(message::set);
        return message.get();
    }
}
