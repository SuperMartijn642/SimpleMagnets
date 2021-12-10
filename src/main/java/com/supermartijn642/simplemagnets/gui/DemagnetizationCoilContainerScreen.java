package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import com.supermartijn642.simplemagnets.packets.demagnetization_coil.*;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class DemagnetizationCoilContainerScreen extends BaseDemagnetizationCoilContainerScreen<DemagnetizationCoilContainer> {

    private UpDownArrowButton upXButton, downXButton;
    private UpDownArrowButton upYButton, downYButton;
    private UpDownArrowButton upZButton, downZButton;

    public DemagnetizationCoilContainerScreen(DemagnetizationCoilContainer container){
        super(container, container.getObjectOrClose().getBlockState().getBlock().getDescriptionId());
    }

    @Override
    protected void addWidgets(DemagnetizationCoilTile demagnetizationCoilTile){
        this.upXButton = this.addWidget(new UpDownArrowButton(24, 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseXRange(this.menu.getTilePos()))));
        this.downXButton = this.addWidget(new UpDownArrowButton(24, 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseXRange(this.menu.getTilePos()))));
        this.upYButton = this.addWidget(new UpDownArrowButton(77, 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseYRange(this.menu.getTilePos()))));
        this.downYButton = this.addWidget(new UpDownArrowButton(77, 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseYRange(this.menu.getTilePos()))));
        this.upZButton = this.addWidget(new UpDownArrowButton(130, 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseZRange(this.menu.getTilePos()))));
        this.downZButton = this.addWidget(new UpDownArrowButton(130, 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseZRange(this.menu.getTilePos()))));
    }

    @Override
    protected void renderTooltips(int mouseX, int mouseY, DemagnetizationCoilTile tile){
    }

    @Override
    protected void tick(DemagnetizationCoilTile tile){
    }

    @Override
    protected String getBackground(){
        return "demagnetization_coil_screen.png";
    }

    @Override
    protected void renderForeground(int mouseX, int mouseY, DemagnetizationCoilTile tile){
        ScreenUtils.drawCenteredString(this.font, this.title, this.imageWidth / 2f, 6, 4210752);

        ScreenUtils.drawString(this.font, TextComponents.translation("gui.simplemagnets.demagnetization_coil.range", (tile.rangeX - 1) * 2 + 1, (tile.rangeY - 1) * 2 + 1, (tile.rangeZ - 1) * 2 + 1).get(), 8, 26, 4210752);
        ScreenUtils.drawCenteredString(this.font, TextComponents.string("x:").get(), 19, 51, 4210752);
        ScreenUtils.drawCenteredString(this.font, TextComponents.number(tile.rangeX).get(), 33, 52, 4210752);
        ScreenUtils.drawCenteredString(this.font, TextComponents.string("y:").get(), 72, 51, 4210752);
        ScreenUtils.drawCenteredString(this.font, TextComponents.number(tile.rangeY).get(), 86, 52, 4210752);
        ScreenUtils.drawCenteredString(this.font, TextComponents.string("z:").get(), 125, 51, 4210752);
        ScreenUtils.drawCenteredString(this.font, TextComponents.number(tile.rangeZ).get(), 139, 52, 4210752);
    }
}
