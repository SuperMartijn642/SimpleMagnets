package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.GuiGraphicsHelper;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.simplemagnets.DemagnetizationCoilBlockEntity;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import com.supermartijn642.simplemagnets.packets.demagnetization_coil.*;
import net.minecraft.resources.Identifier;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class FilteredDemagnetizationCoilContainerScreen extends BaseDemagnetizationCoilContainerScreen<FilteredDemagnetizationCoilContainer> {

    public static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath("simplemagnets", "filtered_demagnetization_coil_screen");

    private WhitelistButton whitelistButton;
    private DurabilityButton durabilityButton;

    @Override
    protected void addWidgets(DemagnetizationCoilBlockEntity entity){
        this.addWidget(new UpDownArrowButton(20, 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseXRange(this.container.getBlockEntityPos()))));
        this.addWidget(new UpDownArrowButton(20, 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseXRange(this.container.getBlockEntityPos()))));
        this.addWidget(new UpDownArrowButton(57, 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseYRange(this.container.getBlockEntityPos()))));
        this.addWidget(new UpDownArrowButton(57, 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseYRange(this.container.getBlockEntityPos()))));
        this.addWidget(new UpDownArrowButton(94, 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseZRange(this.container.getBlockEntityPos()))));
        this.addWidget(new UpDownArrowButton(94, 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseZRange(this.container.getBlockEntityPos()))));
        this.addWidget(new ShowRangeButton(130, 46, () -> this.object.getShowRange(), () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleShowRange(this.container.getBlockEntityPos()))));
        this.whitelistButton = this.addWidget(new WhitelistButton(175, 88, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleWhitelist(this.container.getBlockEntityPos()))));
        this.whitelistButton.update(entity.getFilterWhitelist());
        this.durabilityButton = this.addWidget(new DurabilityButton(197, 88, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleDurability(this.container.getBlockEntityPos()))));
        this.durabilityButton.update(entity.getFilterDurability());
    }

    @Override
    protected void update(DemagnetizationCoilBlockEntity entity){
        this.whitelistButton.update(entity.getFilterWhitelist());
        this.durabilityButton.update(entity.getFilterDurability());
    }

    @Override
    protected Identifier getBackground(){
        return BACKGROUND;
    }

    @SuppressWarnings("Convert2MethodRef")
    @Override
    protected void renderForeground(WidgetRenderContext context, GuiGraphicsHelper graphics, int mouseX, int mouseY, DemagnetizationCoilBlockEntity entity){
        graphics.submitText(TextComponents.block(entity.getBlockState().getBlock()).get(), this.width(entity) / 2f, 6, p -> p.centerHorizontally());
        graphics.submitText(ClientUtils.getPlayer().getInventory().getName(), 32, 112);

        int rangeX = entity.getRangeX(), rangeY = entity.getRangeY(), rangeZ = entity.getRangeZ();
        graphics.submitText(TextComponents.translation("simplemagnets.gui.demagnetization_coil.range", (rangeX - 1) * 2 + 1, (rangeY - 1) * 2 + 1, (rangeZ - 1) * 2 + 1).get(), 8, 26);
        graphics.submitText(TextComponents.string("x:").get(), 15, 51, p -> p.centerHorizontally());
        graphics.submitText(TextComponents.number(rangeX).get(), 29, 52, p -> p.centerHorizontally());
        graphics.submitText(TextComponents.string("y:").get(), 52, 51, p -> p.centerHorizontally());
        graphics.submitText(TextComponents.number(rangeY).get(), 66, 52, p -> p.centerHorizontally());
        graphics.submitText(TextComponents.string("z:").get(), 89, 51, p -> p.centerHorizontally());
        graphics.submitText(TextComponents.number(rangeZ).get(), 103, 52, p -> p.centerHorizontally());
        graphics.submitText(TextComponents.translation("simplemagnets.gui.magnet.filter").get(), 8, 78);
    }
}
