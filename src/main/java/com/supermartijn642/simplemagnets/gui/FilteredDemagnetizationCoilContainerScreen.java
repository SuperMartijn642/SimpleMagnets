package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import com.supermartijn642.simplemagnets.packets.demagnetization_coil.*;

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
        super(container, container.getObjectOrClose().getBlockState().getBlock().getUnlocalizedName() + ".name");
    }

    @Override
    protected void addWidgets(DemagnetizationCoilTile tile){
        this.upXButton = this.addWidget(new UpDownArrowButton(40, 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseXRange(this.container.getTilePos()))));
        this.downXButton = this.addWidget(new UpDownArrowButton(40, 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseXRange(this.container.getTilePos()))));
        this.upYButton = this.addWidget(new UpDownArrowButton(93, 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseYRange(this.container.getTilePos()))));
        this.downYButton = this.addWidget(new UpDownArrowButton(93, 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseYRange(this.container.getTilePos()))));
        this.upZButton = this.addWidget(new UpDownArrowButton(146, 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseZRange(this.container.getTilePos()))));
        this.downZButton = this.addWidget(new UpDownArrowButton(146, 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseZRange(this.container.getTilePos()))));
        this.whitelistButton = this.addWidget(new WhitelistButton(175, 88, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleWhitelist(this.container.getTilePos()))));
        this.whitelistButton.update(tile.filterWhitelist);
        this.durabilityButton = this.addWidget(new DurabilityButton(197, 88, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleDurability(this.container.getTilePos()))));
        this.durabilityButton.update(tile.filterDurability);
    }

    @Override
    protected void renderTooltips(int mouseX, int mouseY, DemagnetizationCoilTile demagnetizationCoilTile){
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
    protected void renderForeground(int mouseX, int mouseY, DemagnetizationCoilTile tile){
        ScreenUtils.drawCenteredString(this.font, this.title, this.xSize / 2f, 6, 4210752);
        ScreenUtils.drawString(this.font, TextComponents.translation("container.inventory").get(), 32, 112, 4210752);

        ScreenUtils.drawString(this.font, TextComponents.translation("gui.simplemagnets.demagnetization_coil.range", (tile.rangeX - 1) * 2 + 1, (tile.rangeY - 1) * 2 + 1, (tile.rangeZ - 1) * 2 + 1).get(), 8, 26, 4210752);
        ScreenUtils.drawCenteredString(this.font, TextComponents.string("x:").get(), 35, 51, 4210752);
        ScreenUtils.drawCenteredString(this.font, TextComponents.number(tile.rangeX).get(), 49, 52, 4210752);
        ScreenUtils.drawCenteredString(this.font, TextComponents.string("y:").get(), 88, 51, 4210752);
        ScreenUtils.drawCenteredString(this.font, TextComponents.number(tile.rangeY).get(), 102, 52, 4210752);
        ScreenUtils.drawCenteredString(this.font, TextComponents.string("z:").get(), 141, 51, 4210752);
        ScreenUtils.drawCenteredString(this.font, TextComponents.number(tile.rangeZ).get(), 155, 52, 4210752);
        ScreenUtils.drawString(this.font, TextComponents.translation("gui.advancedmagnet.filter").get(), 8, 78, 4210752);
    }
}
