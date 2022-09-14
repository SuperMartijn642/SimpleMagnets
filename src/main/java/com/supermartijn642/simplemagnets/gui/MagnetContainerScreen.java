package com.supermartijn642.simplemagnets.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.ItemBaseContainerWidget;
import com.supermartijn642.simplemagnets.SMConfig;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import com.supermartijn642.simplemagnets.packets.magnet.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class MagnetContainerScreen extends ItemBaseContainerWidget<MagnetContainer> {

    private static final ResourceLocation BACKGROUND = new ResourceLocation("simplemagnets", "textures/screen.png");

    private CheckBox itemCheckbox;
    private PlusMinusButton leftItemButton;
    private PlusMinusButton rightItemButton;
    private CheckBox xpCheckbox;
    private PlusMinusButton leftXpButton;
    private PlusMinusButton rightXpButton;
    private WhitelistButton whitelistButton;
    private DurabilityButton durabilityButton;

    private int itemRange = SMConfig.advancedMagnetRange.get();
    private int xpRange = SMConfig.advancedMagnetRange.get();

    public MagnetContainerScreen(){
        super(0, 0, 224, 196, (Supplier<ItemStack>)null, null);
    }

    @Override
    protected Component getNarrationMessage(ItemStack stack){
        return TextComponents.item(stack.getItem()).get();
    }

    @Override
    protected ItemStack getObject(ItemStack oldObject){
        return this.container.getObject(oldObject);
    }

    @Override
    protected boolean validateObject(ItemStack object){
        return this.container.validateObject(object);
    }

    @Override
    protected void addWidgets(ItemStack stack){
        CompoundTag tag = stack.getOrCreateTag();

        this.itemCheckbox = this.addWidget(new CheckBox(11, 38, checked -> checked ? "simplemagnets.gui.magnet.items.on" : "simplemagnets.gui.magnet.items.off", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleItems())));
        this.itemCheckbox.update(!(tag.contains("items") && tag.getBoolean("items")));

        this.leftItemButton = this.addWidget(new PlusMinusButton(54, 38, true, "simplemagnets.gui.magnet.items.decrease", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseItemRange())));
        this.rightItemButton = this.addWidget(new PlusMinusButton(88, 38, false, "simplemagnets.gui.magnet.items.increase", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseItemRange())));

        this.xpCheckbox = this.addWidget(new CheckBox(119, 38, checked -> checked ? "simplemagnets.gui.magnet.xp.on" : "simplemagnets.gui.magnet.xp.off", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleXp())));
        this.xpCheckbox.update(!(tag.contains("xp") && tag.getBoolean("xp")));

        this.leftXpButton = this.addWidget(new PlusMinusButton(162, 38, true, "simplemagnets.gui.magnet.xp.decrease", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseXpRange())));
        this.rightXpButton = this.addWidget(new PlusMinusButton(196, 38, false, "simplemagnets.gui.magnet.xp.increase", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseXpRange())));

        this.whitelistButton = this.addWidget(new WhitelistButton(175, 78, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleMagnetWhitelist())));
        this.whitelistButton.update(tag.contains("whitelist") && tag.getBoolean("whitelist"));

        this.durabilityButton = this.addWidget(new DurabilityButton(197, 78, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleMagnetDurability())));
        this.durabilityButton.update(tag.contains("filterDurability") && tag.getBoolean("filterDurability"));
    }

    @Override
    protected void update(ItemStack stack){
        CompoundTag tag = stack.getOrCreateTag();

        this.itemRange = tag.contains("itemRange") ? tag.getInt("itemRange") : SMConfig.advancedMagnetRange.get();
        this.xpRange = tag.contains("xpRange") ? tag.getInt("xpRange") : SMConfig.advancedMagnetRange.get();

        boolean items = !(tag.contains("items") && tag.getBoolean("items"));
        this.itemCheckbox.update(items);
        this.leftItemButton.active = items && this.itemRange > SMConfig.advancedMagnetMinRange.get();
        this.rightItemButton.active = items && this.itemRange < SMConfig.advancedMagnetMaxRange.get();
        boolean experience = !(tag.contains("xp") && tag.getBoolean("xp"));
        this.xpCheckbox.update(experience);
        this.leftXpButton.active = experience && this.xpRange > SMConfig.advancedMagnetMinRange.get();
        this.rightXpButton.active = experience && this.xpRange < SMConfig.advancedMagnetMaxRange.get();
        this.whitelistButton.update(tag.contains("whitelist") && tag.getBoolean("whitelist"));
        this.durabilityButton.update(tag.contains("filterDurability") && tag.getBoolean("filterDurability"));
    }

    @Override
    protected void renderBackground(PoseStack poseStack, int mouseX, int mouseY, ItemStack stack){
        ScreenUtils.bindTexture(BACKGROUND);
        ScreenUtils.drawTexture(poseStack, 0, 0, this.width(), this.height());

        super.renderBackground(poseStack, mouseX, mouseY, stack);
    }

    @Override
    protected void renderForeground(PoseStack poseStack, int mouseX, int mouseY, ItemStack stack){
        ScreenUtils.drawCenteredString(poseStack, TextComponents.item(stack.getItem()).get(), this.width() / 2f, 6);
        ScreenUtils.drawString(poseStack, ClientUtils.getPlayer().getInventory().getName(), 32, 102);

        ScreenUtils.drawCenteredString(poseStack, TextComponents.translation("simplemagnets.gui.magnet.items").get(), 58, 24);
        ScreenUtils.drawCenteredString(poseStack, TextComponents.translation("simplemagnets.gui.magnet.xp").get(), 166, 24);
        ScreenUtils.drawCenteredString(poseStack, TextComponents.translation("simplemagnets.gui.magnet.filter").get(), 112, 68);

        ScreenUtils.drawCenteredString(poseStack, TextComponents.number(this.itemRange).get(), 79.5f, 43);
        ScreenUtils.drawCenteredString(poseStack, TextComponents.number(this.xpRange).get(), 187.5f, 43);

        super.renderForeground(poseStack, mouseX, mouseY, stack);
    }

    @Override
    protected void renderTooltips(PoseStack poseStack, int mouseX, int mouseY, ItemStack stack){
        if(mouseX > 79.5f - 6 && mouseX < 79.5f + 5 && mouseY > 43 - 2 && mouseY < 43 + 9)
            ScreenUtils.drawTooltip(poseStack, TextComponents.translation("simplemagnets.gui.magnet.items.range", this.itemRange).get(), mouseX, mouseY);
        if(mouseX > 187.5f - 6 && mouseX < 187.5f + 5 && mouseY > 43 - 2 && mouseY < 43 + 9)
            ScreenUtils.drawTooltip(poseStack, TextComponents.translation("simplemagnets.gui.magnet.xp.range", this.xpRange).get(), mouseX, mouseY);

        super.renderTooltips(poseStack, mouseX, mouseY, stack);
    }
}
