package com.supermartijn642.simplemagnets.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
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
public class WhitelistButton extends AbstractButtonWidget {

    private static final ResourceLocation BUTTONS = new ResourceLocation("simplemagnets", "textures/blacklist_button.png");

    public boolean white = true;
    public boolean active = true;

    public WhitelistButton(int x, int y, Runnable onPress){
        super(x, y, 20, 20, onPress);
    }

    public void update(boolean white){
        this.white = white;
    }

    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY){
        ScreenUtils.bindTexture(BUTTONS);
        ScreenUtils.drawTexture(poseStack, this.x, this.y, this.width, this.height, this.white ? 0 : 0.5f, (this.active ? this.isFocused() ? 1 : 0 : 2) / 3f, 0.5f, 1 / 3f);
    }

    @Override
    protected void getTooltips(Consumer<ITextComponent> tooltips){
        tooltips.accept(TextComponents.translation(this.white ? "simplemagnets.gui.magnet.whitelist.on" : "simplemagnets.gui.magnet.whitelist.off").get());
    }

    @Override
    public ITextComponent getNarrationMessage(){
        Holder<ITextComponent> message = new Holder<>();
        this.getTooltips(message::set);
        return message.get();
    }
}
