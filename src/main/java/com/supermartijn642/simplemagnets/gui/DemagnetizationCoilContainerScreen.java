package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.simplemagnets.DemagnetizationCoilBlockEntity;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import com.supermartijn642.simplemagnets.packets.demagnetization_coil.*;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class DemagnetizationCoilContainerScreen extends BaseDemagnetizationCoilContainerScreen<DemagnetizationCoilContainer> {

    @Override
    protected void addWidgets(DemagnetizationCoilBlockEntity entity){
        this.addWidget(new UpDownArrowButton(20, 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseXRange(this.container.getBlockEntityPos()))));
        this.addWidget(new UpDownArrowButton(20, 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseXRange(this.container.getBlockEntityPos()))));
        this.addWidget(new UpDownArrowButton(57, 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseYRange(this.container.getBlockEntityPos()))));
        this.addWidget(new UpDownArrowButton(57, 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseYRange(this.container.getBlockEntityPos()))));
        this.addWidget(new UpDownArrowButton(94, 37, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseZRange(this.container.getBlockEntityPos()))));
        this.addWidget(new UpDownArrowButton(94, 63, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseZRange(this.container.getBlockEntityPos()))));
        this.addWidget(new ShowRangeButton(130, 46, () -> this.object.showRange, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleShowRange(this.container.getBlockEntityPos()))));
    }

    @Override
    protected String getBackground(){
        return "demagnetization_coil_screen.png";
    }

    @Override
    protected void renderForeground(int mouseX, int mouseY, DemagnetizationCoilBlockEntity entity){
        ScreenUtils.drawCenteredString(TextComponents.block(entity.getBlockState().getBlock()).get(), this.width(entity) / 2f, 6);

        ScreenUtils.drawString(TextComponents.translation("simplemagnets.gui.demagnetization_coil.range", (entity.rangeX - 1) * 2 + 1, (entity.rangeY - 1) * 2 + 1, (entity.rangeZ - 1) * 2 + 1).get(), 8, 26);
        ScreenUtils.drawCenteredString(TextComponents.string("x:").get(), 15, 51);
        ScreenUtils.drawCenteredString(TextComponents.number(entity.rangeX).get(), 29, 52);
        ScreenUtils.drawCenteredString(TextComponents.string("y:").get(), 52, 51);
        ScreenUtils.drawCenteredString(TextComponents.number(entity.rangeY).get(), 66, 52);
        ScreenUtils.drawCenteredString(TextComponents.string("z:").get(), 89, 51);
        ScreenUtils.drawCenteredString(TextComponents.number(entity.rangeZ).get(), 103, 52);
    }
}
