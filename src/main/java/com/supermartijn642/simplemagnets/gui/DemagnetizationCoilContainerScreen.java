package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import com.supermartijn642.simplemagnets.packets.demagnetization_coil.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class DemagnetizationCoilContainerScreen extends BaseDemagnetizationCoilContainerScreen<DemagnetizationCoilContainer> {

    private UpDownArrowButton upXButton, downXButton;
    private UpDownArrowButton upYButton, downYButton;
    private UpDownArrowButton upZButton, downZButton;

    public DemagnetizationCoilContainerScreen(DemagnetizationCoilContainer container){
        super(container, container.getTileOrClose().getBlockState().getBlock().getTranslationKey());
    }

    @Override
    protected void addButtons(DemagnetizationCoilTile tile){
        this.upXButton = this.addButton(new UpDownArrowButton(this.guiLeft + 24, this.guiTop + 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseXRange(this.container.pos))));
        this.downXButton = this.addButton(new UpDownArrowButton(this.guiLeft + 24, this.guiTop + 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseXRange(this.container.pos))));
        this.upYButton = this.addButton(new UpDownArrowButton(this.guiLeft + 77, this.guiTop + 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseYRange(this.container.pos))));
        this.downYButton = this.addButton(new UpDownArrowButton(this.guiLeft + 77, this.guiTop + 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseYRange(this.container.pos))));
        this.upZButton = this.addButton(new UpDownArrowButton(this.guiLeft + 130, this.guiTop + 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseZRange(this.container.pos))));
        this.downZButton = this.addButton(new UpDownArrowButton(this.guiLeft + 130, this.guiTop + 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseZRange(this.container.pos))));
    }

    @Override
    protected void drawToolTips(DemagnetizationCoilTile tile, int mouseX, int mouseY){
        if(this.upXButton.isHovered() || this.upYButton.isHovered() || this.upZButton.isHovered())
            this.renderTooltip(true, "gui.simplemagnets.demagnetization_coil.range.increase", mouseX, mouseY);
        if(this.downXButton.isHovered() || this.downYButton.isHovered() || this.downZButton.isHovered())
            this.renderTooltip(true, "gui.simplemagnets.demagnetization_coil.range.decrease", mouseX, mouseY);
    }

    @Override
    protected void tick(DemagnetizationCoilTile tile){
    }

    @Override
    protected String getBackground(){
        return "demagnetization_coil_screen.png";
    }

    @Override
    protected void drawText(DemagnetizationCoilTile tile){
        this.drawCenteredString(this.title, this.xSize / 2f, 6);

        String range = I18n.format("gui.simplemagnets.demagnetization_coil.range")
            .replace("$numberx$", "" + ((tile.rangeX - 1) * 2 + 1))
            .replace("$numbery$", "" + ((tile.rangeY - 1) * 2 + 1))
            .replace("$numberz$", "" + ((tile.rangeZ - 1) * 2 + 1));
        this.drawString(new StringTextComponent(range), 8, 26);
        this.drawCenteredString(new StringTextComponent("x:"), 19, 51);
        this.drawCenteredString(new StringTextComponent("" + tile.rangeX), 33, 52);
        this.drawCenteredString(new StringTextComponent("y:"), 72, 51);
        this.drawCenteredString(new StringTextComponent("" + tile.rangeY), 86, 52);
        this.drawCenteredString(new StringTextComponent("z:"), 125, 51);
        this.drawCenteredString(new StringTextComponent("" + tile.rangeZ), 139, 52);
    }
}
