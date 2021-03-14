package com.supermartijn642.simplemagnets.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import com.supermartijn642.simplemagnets.packets.demagnetization_coil.*;
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
        super(container, container.getObjectOrClose().getBlockState().getBlock().getTranslationKey());
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
    protected void renderTooltips(MatrixStack matrixStack, int mouseX, int mouseY, DemagnetizationCoilTile demagnetizationCoilTile){
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
    protected void renderForeground(MatrixStack matrixStack, int mouseX, int mouseY, DemagnetizationCoilTile tile){
        ScreenUtils.drawCenteredString(matrixStack, this.font, this.title, this.xSize / 2f, 6, 4210752);
        ScreenUtils.drawString(matrixStack, this.font, this.playerInventory.getDisplayName(), 32, 112, 4210752);

        ScreenUtils.drawString(matrixStack, this.font, new TranslationTextComponent("gui.simplemagnets.demagnetization_coil.range", (tile.rangeX - 1) * 2 + 1, (tile.rangeY - 1) * 2 + 1, (tile.rangeZ - 1) * 2 + 1), 8, 26, 4210752);
        ScreenUtils.drawCenteredString(matrixStack, this.font, new StringTextComponent("x:"), 35, 51, 4210752);
        ScreenUtils.drawCenteredString(matrixStack, this.font, new StringTextComponent("" + tile.rangeX), 49, 52, 4210752);
        ScreenUtils.drawCenteredString(matrixStack, this.font, new StringTextComponent("y:"), 88, 51, 4210752);
        ScreenUtils.drawCenteredString(matrixStack, this.font, new StringTextComponent("" + tile.rangeY), 102, 52, 4210752);
        ScreenUtils.drawCenteredString(matrixStack, this.font, new StringTextComponent("z:"), 141, 51, 4210752);
        ScreenUtils.drawCenteredString(matrixStack, this.font, new StringTextComponent("" + tile.rangeZ), 155, 52, 4210752);
        ScreenUtils.drawString(matrixStack, this.font, new TranslationTextComponent("gui.advancedmagnet.filter"), 8, 78, 4210752);
    }
}
