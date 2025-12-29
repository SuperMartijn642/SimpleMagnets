package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.GuiGraphicsHelper;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import com.supermartijn642.core.util.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class CheckBox extends AbstractButtonWidget {

    public static final Identifier BUTTONS = Identifier.fromNamespaceAndPath("simplemagnets", "checkmarkbox");

    private final Function<Boolean,String> translationKey;
    public boolean checked;
    public boolean active = true;

    public CheckBox(int x, int y, Function<Boolean,String> translationKey, Runnable onPress){
        super(x, y, 17, 17, onPress);
        this.translationKey = translationKey;
    }

    public void update(boolean checked){
        this.checked = checked;
    }

    @Override
    public void render(WidgetRenderContext context, GuiGraphicsHelper graphics, int mouseX, int mouseY){
        graphics.submitSprite(BUTTONS, this.x, this.y - 3, this.width + 3, this.height + 3, p -> p.uv(this.checked ? 0 : 0.5f, (this.active ? this.isFocused() ? 1 : 0 : 2) / 3f, 0.5f, 1 / 3f));
    }

    @Override
    protected void getTooltips(Consumer<Component> tooltips){
        tooltips.accept(TextComponents.translation(this.translationKey.apply(this.checked)).get());
    }

    @Override
    public Component getNarrationMessage(){
        Holder<Component> message = new Holder<>();
        this.getTooltips(message::set);
        return message.get();
    }
}
