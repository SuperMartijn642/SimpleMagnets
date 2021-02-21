package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import com.supermartijn642.simplemagnets.packets.demagnetization_coil.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class FilteredDemagnetizationCoilContainerScreen extends BaseDemagnetizationCoilContainerScreen<FilteredDemagnetizationCoilContainer> {

    private UpDownArrowButton upXButton, downXButton;
    private UpDownArrowButton upYButton, downYButton;
    private UpDownArrowButton upZButton, downZButton;
    private WhitelistButton whitelistButton;
    private DurabilityButton durabilityButton;

    public FilteredDemagnetizationCoilContainerScreen(FilteredDemagnetizationCoilContainer container){
        super(container, container.getTileOrClose().getBlockState().getBlock().getUnlocalizedName());
    }

    @Override
    protected void addButtons(DemagnetizationCoilTile tile){
        this.upXButton = this.addButton(new UpDownArrowButton(1, this.guiLeft + 40, this.guiTop + 37, false, () -> SimpleMagnets.channel.sendToServer(new PacketIncreaseXRange(this.container.pos))));
        this.downXButton = this.addButton(new UpDownArrowButton(2, this.guiLeft + 40, this.guiTop + 63, true, () -> SimpleMagnets.channel.sendToServer(new PacketDecreaseXRange(this.container.pos))));
        this.upYButton = this.addButton(new UpDownArrowButton(3, this.guiLeft + 93, this.guiTop + 37, false, () -> SimpleMagnets.channel.sendToServer(new PacketIncreaseYRange(this.container.pos))));
        this.downYButton = this.addButton(new UpDownArrowButton(4, this.guiLeft + 93, this.guiTop + 63, true, () -> SimpleMagnets.channel.sendToServer(new PacketDecreaseYRange(this.container.pos))));
        this.upZButton = this.addButton(new UpDownArrowButton(5, this.guiLeft + 146, this.guiTop + 37, false, () -> SimpleMagnets.channel.sendToServer(new PacketIncreaseZRange(this.container.pos))));
        this.downZButton = this.addButton(new UpDownArrowButton(6, this.guiLeft + 146, this.guiTop + 63, true, () -> SimpleMagnets.channel.sendToServer(new PacketDecreaseZRange(this.container.pos))));
        this.whitelistButton = this.addButton(new WhitelistButton(7, this.guiLeft + 175, this.guiTop + 88, () -> SimpleMagnets.channel.sendToServer(new PacketToggleWhitelist(this.container.pos))));
        this.whitelistButton.update(tile.filterWhitelist);
        this.durabilityButton = this.addButton(new DurabilityButton(8, this.guiLeft + 197, this.guiTop + 88, () -> SimpleMagnets.channel.sendToServer(new PacketToggleDurability(this.container.pos))));
        this.durabilityButton.update(tile.filterDurability);
    }

    @Override
    protected void drawToolTips(DemagnetizationCoilTile tile, int mouseX, int mouseY){
        if(this.upXButton.isMouseOver() || this.upYButton.isMouseOver() || this.upZButton.isMouseOver())
            this.renderToolTip(true, "gui.simplemagnets.demagnetization_coil.range.increase", mouseX, mouseY);
        if(this.downXButton.isMouseOver() || this.downYButton.isMouseOver() || this.downZButton.isMouseOver())
            this.renderToolTip(true, "gui.simplemagnets.demagnetization_coil.range.decrease", mouseX, mouseY);
        if(this.whitelistButton.isMouseOver())
            this.renderToolTip(true, "gui.advancedmagnet.whitelist." + (tile.filterWhitelist ? "on" : "off"), mouseX, mouseY);
        if(this.durabilityButton.isMouseOver())
            this.renderToolTip(true, "gui.simplemagnets.demagnetization_coil.durability." + (tile.filterDurability ? "on" : "off"), mouseX, mouseY);
    }

    @Override
    protected void tick(DemagnetizationCoilTile tile){
        this.whitelistButton.update(tile.filterWhitelist);
        this.durabilityButton.update(tile.filterDurability);
    }

    @Override
    protected String getBackground(){
        return "filtered_demagnetization_coil_screen.png";
    }

    @Override
    protected void drawText(DemagnetizationCoilTile tile){
        this.drawCenteredString(this.title, this.xSize / 2, 6);
        this.drawString(this.container.player.inventory.getDisplayName(), 32, 112);

        String range = I18n.format("gui.simplemagnets.demagnetization_coil.range")
            .replace("$numberx$", "" + ((tile.rangeX - 1) * 2 + 1))
            .replace("$numbery$", "" + ((tile.rangeY - 1) * 2 + 1))
            .replace("$numberz$", "" + ((tile.rangeZ - 1) * 2 + 1));
        this.drawString(new TextComponentString(range), 8, 26);
        this.drawCenteredString(new TextComponentString("x:"), 35, 51);
        this.drawCenteredString(new TextComponentString("" + tile.rangeX), 49, 52);
        this.drawCenteredString(new TextComponentString("y:"), 88, 51);
        this.drawCenteredString(new TextComponentString("" + tile.rangeY), 102, 52);
        this.drawCenteredString(new TextComponentString("z:"), 141, 51);
        this.drawCenteredString(new TextComponentString("" + tile.rangeZ), 155, 52);
        this.drawString(new TextComponentTranslation("gui.advancedmagnet.filter"), 8, 78);
    }
}
