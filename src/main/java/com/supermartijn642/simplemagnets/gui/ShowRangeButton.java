package com.supermartijn642.simplemagnets.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class ShowRangeButton extends AbstractButtonWidget {

    private static final ResourceLocation BUTTONS = new ResourceLocation("simplemagnets", "textures/visualize_button.png");

    private final Supplier<Boolean> on;
    public boolean active = true;

    public ShowRangeButton(int x, int y, Supplier<Boolean> on, Runnable onPress){
        super(x, y, 20, 20, onPress);
        this.on = on;
    }

    @Override
    public ITextComponent getNarrationMessage(){
        return TextComponents.translation("simplemagnets.gui.demagnetization_coil.show_range",
            this.on.get() ? TextComponents.translation("simplemagnets.gui.demagnetization_coil.show_range.on").color(TextFormatting.GREEN).get() : TextComponents.translation("simplemagnets.gui.demagnetization_coil.show_range.off").color(TextFormatting.RED).get()
        ).get();
    }

    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY){
        ScreenUtils.bindTexture(BUTTONS);
        ScreenUtils.drawTexture(poseStack, this.x, this.y, this.width, this.height, this.on.get() ? 0 : 0.5f, this.active ? this.isFocused() ? 1 / 3f : 0 : 2 / 3f, 0.5f, 1 / 3f);
    }

    @Override
    protected void getTooltips(Consumer<ITextComponent> tooltips){
        tooltips.accept(this.getNarrationMessage());
    }
}
