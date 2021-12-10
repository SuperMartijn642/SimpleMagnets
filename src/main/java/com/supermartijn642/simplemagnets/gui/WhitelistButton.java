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
public class WhitelistButton extends AbstractButtonWidget implements IHoverTextWidget {

    private final ResourceLocation BUTTONS = new ResourceLocation("simplemagnets", "textures/blacklist_button.png");

    public boolean white = true;

    public WhitelistButton(int x, int y, Runnable onPress){
        super(x, y, 20, 20, onPress);
    }

    public void update(boolean white){
        this.white = white;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks){
        ScreenUtils.bindTexture(BUTTONS);
        ScreenUtils.drawTexture(this.x, this.y, this.width, this.height, this.white ? 0 : 0.5f, (this.active ? this.hovered ? 1 : 0 : 2) / 3f, 0.5f, 1 / 3f);
    }

    @Override
    public ITextComponent getHoverText(){
        return TextComponents.translation(this.white ? "gui.advancedmagnet.whitelist.on" : "gui.advancedmagnet.whitelist.off").get();
    }

    @Override
    protected ITextComponent getNarrationMessage(){
        return this.getHoverText();
    }
}
