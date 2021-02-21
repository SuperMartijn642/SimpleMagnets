package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import com.supermartijn642.simplemagnets.packets.demagnetization_coil.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
        super(container, container.getTileOrClose().getBlockState().getBlock().getTranslationKey());
    }

    @Override
    protected void addButtons(DemagnetizationCoilTile tile){
        this.upXButton = this.addButton(new UpDownArrowButton(this.guiLeft + 40, this.guiTop + 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseXRange(this.container.pos))));
        this.downXButton = this.addButton(new UpDownArrowButton(this.guiLeft + 40, this.guiTop + 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseXRange(this.container.pos))));
        this.upYButton = this.addButton(new UpDownArrowButton(this.guiLeft + 93, this.guiTop + 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseYRange(this.container.pos))));
        this.downYButton = this.addButton(new UpDownArrowButton(this.guiLeft + 93, this.guiTop + 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseYRange(this.container.pos))));
        this.upZButton = this.addButton(new UpDownArrowButton(this.guiLeft + 146, this.guiTop + 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseZRange(this.container.pos))));
        this.downZButton = this.addButton(new UpDownArrowButton(this.guiLeft + 146, this.guiTop + 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseZRange(this.container.pos))));
        this.whitelistButton = this.addButton(new WhitelistButton(this.guiLeft + 175, this.guiTop + 88, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleWhitelist(this.container.pos))));
        this.whitelistButton.update(tile.filterWhitelist);
        this.durabilityButton = this.addButton(new DurabilityButton(this.guiLeft + 197, this.guiTop + 88, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleDurability(this.container.pos))));
        this.durabilityButton.update(tile.filterDurability);
    }

    @Override
    protected void drawToolTips(DemagnetizationCoilTile tile, int mouseX, int mouseY){
        if(this.upXButton.isHovered() || this.upYButton.isHovered() || this.upZButton.isHovered())
            this.renderTooltip(true, "gui.simplemagnets.demagnetization_coil.range.increase", mouseX, mouseY);
        if(this.downXButton.isHovered() || this.downYButton.isHovered() || this.downZButton.isHovered())
            this.renderTooltip(true, "gui.simplemagnets.demagnetization_coil.range.decrease", mouseX, mouseY);
        if(this.whitelistButton.isHovered())
            this.renderTooltip(true, "gui.advancedmagnet.whitelist." + (tile.filterWhitelist ? "on" : "off"), mouseX, mouseY);
        if(this.durabilityButton.isHovered())
            this.renderTooltip(true, "gui.simplemagnets.demagnetization_coil.durability." + (tile.filterDurability ? "on" : "off"), mouseX, mouseY);
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
        this.drawCenteredString(this.title, this.xSize / 2f, 6);
        this.drawString(this.playerInventory.getDisplayName(), 32, 112);

        String range = I18n.format("gui.simplemagnets.demagnetization_coil.range")
            .replace("$numberx$", "" + ((tile.rangeX - 1) * 2 + 1))
            .replace("$numbery$", "" + ((tile.rangeY - 1) * 2 + 1))
            .replace("$numberz$", "" + ((tile.rangeZ - 1) * 2 + 1));
        this.drawString(new StringTextComponent(range), 8, 26);
        this.drawCenteredString(new StringTextComponent("x:"), 35, 51);
        this.drawCenteredString(new StringTextComponent("" + tile.rangeX), 49, 52);
        this.drawCenteredString(new StringTextComponent("y:"), 88, 51);
        this.drawCenteredString(new StringTextComponent("" + tile.rangeY), 102, 52);
        this.drawCenteredString(new StringTextComponent("z:"), 141, 51);
        this.drawCenteredString(new StringTextComponent("" + tile.rangeZ), 155, 52);
        this.drawString(new TranslationTextComponent("gui.advancedmagnet.filter"), 8, 78);
    }
}
