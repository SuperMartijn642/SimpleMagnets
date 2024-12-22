package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.ItemBaseContainerWidget;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.simplemagnets.AdvancedMagnet;
import com.supermartijn642.simplemagnets.SMConfig;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import com.supermartijn642.simplemagnets.packets.magnet.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class MagnetContainerScreen extends ItemBaseContainerWidget<MagnetContainer> {

    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath("simplemagnets", "textures/screen.png");

    private CheckBox itemCheckbox;
    private PlusMinusButton leftItemButton;
    private PlusMinusButton rightItemButton;
    private CheckBox xpCheckbox;
    private PlusMinusButton leftXpButton;
    private PlusMinusButton rightXpButton;
    private WhitelistButton whitelistButton;
    private DurabilityButton durabilityButton;

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
        AdvancedMagnet.Settings settings = stack.get(AdvancedMagnet.SETTINGS);
        if(settings == null)
            settings = AdvancedMagnet.Settings.defaultSettings();

        this.itemCheckbox = this.addWidget(new CheckBox(11, 38, checked -> checked ? "simplemagnets.gui.magnet.items.on" : "simplemagnets.gui.magnet.items.off", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleItems())));
        this.itemCheckbox.update(settings.collectItems());

        this.leftItemButton = this.addWidget(new PlusMinusButton(54, 38, true, "simplemagnets.gui.magnet.items.decrease", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseItemRange())));
        this.rightItemButton = this.addWidget(new PlusMinusButton(88, 38, false, "simplemagnets.gui.magnet.items.increase", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseItemRange())));

        this.xpCheckbox = this.addWidget(new CheckBox(119, 38, checked -> checked ? "simplemagnets.gui.magnet.xp.on" : "simplemagnets.gui.magnet.xp.off", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleXp())));
        this.xpCheckbox.update(settings.collectXp());

        this.leftXpButton = this.addWidget(new PlusMinusButton(162, 38, true, "simplemagnets.gui.magnet.xp.decrease", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseXpRange())));
        this.rightXpButton = this.addWidget(new PlusMinusButton(196, 38, false, "simplemagnets.gui.magnet.xp.increase", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseXpRange())));

        this.whitelistButton = this.addWidget(new WhitelistButton(175, 78, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleMagnetWhitelist())));
        this.whitelistButton.update(settings.isWhitelist());

        this.durabilityButton = this.addWidget(new DurabilityButton(197, 78, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleMagnetDurability())));
        this.durabilityButton.update(settings.isFilterDurability());
    }

    @Override
    protected void update(ItemStack stack){
        AdvancedMagnet.Settings settings = stack.get(AdvancedMagnet.SETTINGS);
        if(settings == null)
            settings = AdvancedMagnet.Settings.defaultSettings();

        this.itemCheckbox.update(settings.collectItems());
        this.leftItemButton.active = settings.collectItems() && settings.itemRange() > SMConfig.advancedMagnetMinRange.get();
        this.rightItemButton.active = settings.collectItems() && settings.itemRange() < SMConfig.advancedMagnetMaxRange.get();
        this.xpCheckbox.update(settings.collectXp());
        this.leftXpButton.active = settings.collectXp() && settings.xpRange() > SMConfig.advancedMagnetMinRange.get();
        this.rightXpButton.active = settings.collectXp() && settings.xpRange() < SMConfig.advancedMagnetMaxRange.get();
        this.whitelistButton.update(settings.isWhitelist());
        this.durabilityButton.update(settings.isFilterDurability());
    }

    @Override
    protected void renderBackground(WidgetRenderContext context, int mouseX, int mouseY, ItemStack stack){
        ScreenUtils.drawTexture(BACKGROUND, context.poseStack(), 0, 0, this.width(), this.height());
        super.renderBackground(context, mouseX, mouseY, stack);
    }

    @Override
    protected void renderForeground(WidgetRenderContext context, int mouseX, int mouseY, ItemStack stack){
        ScreenUtils.drawCenteredString(context.poseStack(), TextComponents.item(stack.getItem()).get(), this.width() / 2f, 6);
        ScreenUtils.drawString(context.poseStack(), ClientUtils.getPlayer().getInventory().getName(), 32, 102);

        ScreenUtils.drawCenteredString(context.poseStack(), TextComponents.translation("simplemagnets.gui.magnet.items").get(), 58, 24);
        ScreenUtils.drawCenteredString(context.poseStack(), TextComponents.translation("simplemagnets.gui.magnet.xp").get(), 166, 24);
        ScreenUtils.drawCenteredString(context.poseStack(), TextComponents.translation("simplemagnets.gui.magnet.filter").get(), 112, 68);

        AdvancedMagnet.Settings settings = stack.get(AdvancedMagnet.SETTINGS);
        if(settings == null)
            settings = AdvancedMagnet.Settings.defaultSettings();
        ScreenUtils.drawCenteredString(context.poseStack(), TextComponents.number(settings.itemRange()).get(), 79.5f, 43);
        ScreenUtils.drawCenteredString(context.poseStack(), TextComponents.number(settings.xpRange()).get(), 187.5f, 43);

        super.renderForeground(context, mouseX, mouseY, stack);
    }

    @Override
    protected void renderTooltips(WidgetRenderContext context, int mouseX, int mouseY, ItemStack stack){
        AdvancedMagnet.Settings settings = stack.get(AdvancedMagnet.SETTINGS);
        if(settings == null)
            settings = AdvancedMagnet.Settings.defaultSettings();
        if(mouseX > 79.5f - 6 && mouseX < 79.5f + 5 && mouseY > 43 - 2 && mouseY < 43 + 9)
            ScreenUtils.drawTooltip(context.poseStack(), TextComponents.translation("simplemagnets.gui.magnet.items.range", settings.itemRange()).get(), mouseX, mouseY);
        if(mouseX > 187.5f - 6 && mouseX < 187.5f + 5 && mouseY > 43 - 2 && mouseY < 43 + 9)
            ScreenUtils.drawTooltip(context.poseStack(), TextComponents.translation("simplemagnets.gui.magnet.xp.range", settings.xpRange()).get(), mouseX, mouseY);

        super.renderTooltips(context, mouseX, mouseY, stack);
    }
}
