package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import com.supermartijn642.core.util.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class PlusMinusButton extends AbstractButtonWidget {

    private static final ResourceLocation BUTTONS = ResourceLocation.fromNamespaceAndPath("simplemagnets", "textures/plus_minus_buttons.png");

    private final boolean left;
    private final String translationKey;
    public boolean active = true;

    public PlusMinusButton(int x, int y, boolean left, String translationKey, Runnable onPress){
        super(x, y, 17, 17, onPress);
        this.left = left;
        this.translationKey = translationKey;
    }

    @Override
    protected void getTooltips(Consumer<Component> tooltips){
        tooltips.accept(TextComponents.translation(this.translationKey).get());
    }

    @Override
    public Component getNarrationMessage(){
        Holder<Component> message = new Holder<>();
        this.getTooltips(message::set);
        return message.get();
    }

    @Override
    public void render(WidgetRenderContext context, int mouseX, int mouseY){
        ScreenUtils.drawTexture(BUTTONS, context.poseStack(), this.x, this.y, this.width, this.height, this.left ? 0.5f : 0, (this.active ? this.isFocused() ? 1 : 0 : 2) / 3f, 0.5f, 1 / 3f);
    }
}
