package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import com.supermartijn642.simplemagnets.packets.demagnetization_coil.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class DemagnetizationCoilContainerScreen extends BaseDemagnetizationCoilContainerScreen<DemagnetizationCoilContainer> {

    private UpDownArrowButton upXButton, downXButton;
    private UpDownArrowButton upYButton, downYButton;
    private UpDownArrowButton upZButton, downZButton;

    public DemagnetizationCoilContainerScreen(DemagnetizationCoilContainer container){
        super(container, container.getTileOrClose().getBlockState().getBlock().getUnlocalizedName());
    }

    @Override
    protected void addButtons(DemagnetizationCoilTile tile){
        this.upXButton = this.addButton(new UpDownArrowButton(1, this.guiLeft + 24, this.guiTop + 37, false, () -> SimpleMagnets.channel.sendToServer(new PacketIncreaseXRange(this.container.pos))));
        this.downXButton = this.addButton(new UpDownArrowButton(2, this.guiLeft + 24, this.guiTop + 63, true, () -> SimpleMagnets.channel.sendToServer(new PacketDecreaseXRange(this.container.pos))));
        this.upYButton = this.addButton(new UpDownArrowButton(3, this.guiLeft + 77, this.guiTop + 37, false, () -> SimpleMagnets.channel.sendToServer(new PacketIncreaseYRange(this.container.pos))));
        this.downYButton = this.addButton(new UpDownArrowButton(4, this.guiLeft + 77, this.guiTop + 63, true, () -> SimpleMagnets.channel.sendToServer(new PacketDecreaseYRange(this.container.pos))));
        this.upZButton = this.addButton(new UpDownArrowButton(5, this.guiLeft + 130, this.guiTop + 37, false, () -> SimpleMagnets.channel.sendToServer(new PacketIncreaseZRange(this.container.pos))));
        this.downZButton = this.addButton(new UpDownArrowButton(6, this.guiLeft + 130, this.guiTop + 63, true, () -> SimpleMagnets.channel.sendToServer(new PacketDecreaseZRange(this.container.pos))));
    }

    @Override
    protected void drawToolTips(DemagnetizationCoilTile tile, int mouseX, int mouseY){
        if(this.upXButton.isMouseOver() || this.upYButton.isMouseOver() || this.upZButton.isMouseOver())
            this.renderToolTip(true, "gui.simplemagnets.demagnetization_coil.range.increase", mouseX, mouseY);
        if(this.downXButton.isMouseOver() || this.downYButton.isMouseOver() || this.downZButton.isMouseOver())
            this.renderToolTip(true, "gui.simplemagnets.demagnetization_coil.range.decrease", mouseX, mouseY);
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
        this.drawCenteredString(this.title, this.xSize / 2, 6);

        String range = I18n.format("gui.simplemagnets.demagnetization_coil.range")
            .replace("$numberx$", "" + ((tile.rangeX - 1) * 2 + 1))
            .replace("$numbery$", "" + ((tile.rangeY - 1) * 2 + 1))
            .replace("$numberz$", "" + ((tile.rangeZ - 1) * 2 + 1));
        this.drawString(new TextComponentString(range), 8, 26);
        this.drawCenteredString(new TextComponentString("x:"), 19, 51);
        this.drawCenteredString(new TextComponentString("" + tile.rangeX), 33, 52);
        this.drawCenteredString(new TextComponentString("y:"), 72, 51);
        this.drawCenteredString(new TextComponentString("" + tile.rangeY), 86, 52);
        this.drawCenteredString(new TextComponentString("z:"), 125, 51);
        this.drawCenteredString(new TextComponentString("" + tile.rangeZ), 139, 52);
    }
}
