package com.supermartijn642.simplemagnets.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.AbstractButtonWidget;
import com.supermartijn642.core.gui.widget.IHoverTextWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class CheckBox extends AbstractButtonWidget implements IHoverTextWidget {

    private final ResourceLocation BUTTONS = new ResourceLocation("simplemagnets", "textures/checkmarkbox.png");

    public boolean checked;
    private final Function<Boolean,String> translationKey;

    public CheckBox(int x, int y, Function<Boolean,String> translationKey, Runnable onPress){
        super(x, y, 17, 17, onPress);
        this.translationKey = translationKey;
    }

    public void update(boolean checked){
        this.checked = checked;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks){
        ScreenUtils.bindTexture(BUTTONS);
        ScreenUtils.drawTexture(matrixStack, this.x, this.y - 3, this.width + 3, this.height + 3, this.checked ? 0 : 0.5f, (this.active ? this.hovered ? 1 : 0 : 2) / 3f, 0.5f, 1 / 3f);
    }

    @Override
    public Component getHoverText(){
        return TextComponents.translation(this.translationKey.apply(this.checked)).get();
    }

    @Override
    protected Component getNarrationMessage(){
        return this.getHoverText();
    }
}
