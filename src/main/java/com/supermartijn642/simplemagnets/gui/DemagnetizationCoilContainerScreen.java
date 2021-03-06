package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import com.supermartijn642.simplemagnets.packets.demagnetization_coil.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class DemagnetizationCoilContainerScreen extends BaseDemagnetizationCoilContainerScreen<DemagnetizationCoilContainer> {

    private UpDownArrowButton upXButton, downXButton;
    private UpDownArrowButton upYButton, downYButton;
    private UpDownArrowButton upZButton, downZButton;

    public DemagnetizationCoilContainerScreen(DemagnetizationCoilContainer container){
        super(container, container.getObjectOrClose().getBlockState().getBlock().getTranslationKey());
    }

    @Override
    protected void addWidgets(DemagnetizationCoilTile demagnetizationCoilTile){
        this.upXButton = this.addWidget(new UpDownArrowButton(24, 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseXRange(this.container.getTilePos()))));
        this.downXButton = this.addWidget(new UpDownArrowButton(24, 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseXRange(this.container.getTilePos()))));
        this.upYButton = this.addWidget(new UpDownArrowButton(77, 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseYRange(this.container.getTilePos()))));
        this.downYButton = this.addWidget(new UpDownArrowButton(77, 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseYRange(this.container.getTilePos()))));
        this.upZButton = this.addWidget(new UpDownArrowButton(130, 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseZRange(this.container.getTilePos()))));
        this.downZButton = this.addWidget(new UpDownArrowButton(130, 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseZRange(this.container.getTilePos()))));
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
        ScreenUtils.drawCenteredString(this.font, this.title, this.xSize / 2f, 6, 4210752);

        ScreenUtils.drawString(this.font, new TranslationTextComponent("gui.simplemagnets.demagnetization_coil.range", (tile.rangeX - 1) * 2 + 1, (tile.rangeY - 1) * 2 + 1, (tile.rangeZ - 1) * 2 + 1), 8, 26, 4210752);
        ScreenUtils.drawCenteredString(this.font, new StringTextComponent("x:"), 19, 51, 4210752);
        ScreenUtils.drawCenteredString(this.font, new StringTextComponent("" + tile.rangeX), 33, 52, 4210752);
        ScreenUtils.drawCenteredString(this.font, new StringTextComponent("y:"), 72, 51, 4210752);
        ScreenUtils.drawCenteredString(this.font, new StringTextComponent("" + tile.rangeY), 86, 52, 4210752);
        ScreenUtils.drawCenteredString(this.font, new StringTextComponent("z:"), 125, 51, 4210752);
        ScreenUtils.drawCenteredString(this.font, new StringTextComponent("" + tile.rangeZ), 139, 52, 4210752);
    }
}
