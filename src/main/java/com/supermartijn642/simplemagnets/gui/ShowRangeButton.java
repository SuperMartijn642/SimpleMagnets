package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.GuiGraphicsHelper;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class ShowRangeButton extends AbstractButtonWidget {

    public static final Identifier BUTTONS = Identifier.fromNamespaceAndPath("simplemagnets", "visualize_button");

    private final Supplier<Boolean> on;
    public boolean active = true;

    public ShowRangeButton(int x, int y, Supplier<Boolean> on, Runnable onPress){
        super(x, y, 20, 20, onPress);
        this.on = on;
    }

    @Override
    public Component getNarrationMessage(){
        return TextComponents.translation("simplemagnets.gui.demagnetization_coil.show_range",
            this.on.get() ? TextComponents.translation("simplemagnets.gui.demagnetization_coil.show_range.on").color(ChatFormatting.GREEN).get() : TextComponents.translation("simplemagnets.gui.demagnetization_coil.show_range.off").color(ChatFormatting.RED).get()
        ).get();
    }

    @Override
    public void render(WidgetRenderContext context, GuiGraphicsHelper graphics, int mouseX, int mouseY){
        graphics.submitSprite(BUTTONS, this.x, this.y, this.width, this.height, p -> p.uv(this.on.get() ? 0 : 0.5f, this.active ? this.isFocused() ? 1 / 3f : 0 : 2 / 3f, 0.5f, 1 / 3f));
    }

    @Override
    protected void getTooltips(Consumer<Component> tooltips){
        tooltips.accept(this.getNarrationMessage());
    }
}
