package com.supermartijn642.simplemagnets.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.gui.ItemBaseContainerScreen;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.simplemagnets.AdvancedMagnet;
import com.supermartijn642.simplemagnets.SMConfig;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import com.supermartijn642.simplemagnets.packets.magnet.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class MagnetContainerScreen extends ItemBaseContainerScreen<MagnetContainer> {

    private final ResourceLocation BACKGROUND = new ResourceLocation("simplemagnets", "textures/screen.png");

    private CheckBox itemCheckbox;
    private LeftRightArrowButton leftItemButton;
    private LeftRightArrowButton rightItemButton;
    private CheckBox xpCheckbox;
    private LeftRightArrowButton leftXpButton;
    private LeftRightArrowButton rightXpButton;
    private WhitelistButton whitelistButton;
    private DurabilityButton durabilityButton;

    private int itemRange = SMConfig.advancedMagnetRange.get();
    private int xpRange = SMConfig.advancedMagnetRange.get();

    public MagnetContainerScreen(MagnetContainer container, Inventory inv, Component title){
        super(container, title);
    }

    @Override
    protected int sizeX(ItemStack stack){
        return 224;
    }

    @Override
    protected int sizeY(ItemStack stack){
        return 196;
    }

    @Override
    protected void addWidgets(ItemStack stack){
        CompoundTag tag = stack.getOrCreateTag();

        this.itemCheckbox = this.addWidget(new CheckBox(19, 39, checked -> checked ? "gui.advancedmagnet.items.on" : "gui.advancedmagnet.items.off", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleItems())));
        this.itemCheckbox.update(!(tag.contains("items") && tag.getBoolean("items")));

        this.leftItemButton = this.addWidget(new LeftRightArrowButton(45, 39, true, "gui.advancedmagnet.items.decrease", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseItemRange())));

        this.rightItemButton = this.addWidget(new LeftRightArrowButton(78, 39, false, "gui.advancedmagnet.items.increase", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseItemRange())));

        this.xpCheckbox = this.addWidget(new CheckBox(113, 39, checked -> checked ? "gui.advancedmagnet.xp.on" : "gui.advancedmagnet.xp.off", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleXp())));
        this.xpCheckbox.update(!(tag.contains("xp") && tag.getBoolean("xp")));

        this.leftXpButton = this.addWidget(new LeftRightArrowButton(139, 39, true, "gui.advancedmagnet.xp.decrease", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseXpRange())));

        this.rightXpButton = this.addWidget(new LeftRightArrowButton(172, 39, false, "gui.advancedmagnet.xp.increase", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseXpRange())));

        this.whitelistButton = this.addWidget(new WhitelistButton(175, 78, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleMagnetWhitelist())));
        this.whitelistButton.update(tag.contains("whitelist") && tag.getBoolean("whitelist"));

        this.durabilityButton = this.addWidget(new DurabilityButton(197, 78, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleMagnetDurability())));
        this.durabilityButton.update(tag.contains("filterDurability") && tag.getBoolean("filterDurability"));
    }

    @Override
    protected void containerTick(@Nonnull ItemStack stack){
        CompoundTag tag = stack.getOrCreateTag();

        this.itemCheckbox.update(!(tag.contains("items") && tag.getBoolean("items")));
        this.xpCheckbox.update(!(tag.contains("xp") && tag.getBoolean("xp")));
        this.whitelistButton.update(tag.contains("whitelist") && tag.getBoolean("whitelist"));
        this.durabilityButton.update(tag.contains("filterDurability") && tag.getBoolean("filterDurability"));

        this.itemRange = tag.contains("itemRange") ? tag.getInt("itemRange") : SMConfig.advancedMagnetRange.get();
        this.xpRange = tag.contains("xpRange") ? tag.getInt("xpRange") : SMConfig.advancedMagnetRange.get();
    }

    @Override
    protected void renderBackground(PoseStack matrixStack, int mouseX, int mouseY, ItemStack object){
        ScreenUtils.bindTexture(BACKGROUND);
        ScreenUtils.drawTexture(matrixStack, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderForeground(PoseStack matrixStack, int mouseX, int mouseY, ItemStack object){
        ScreenUtils.drawCenteredString(matrixStack, this.font, this.title, this.imageWidth / 2f, 6, 4210752);
        ScreenUtils.drawString(matrixStack, this.font, this.playerInventoryTitle, 32, 102, 4210752);

        ScreenUtils.drawCenteredString(matrixStack, this.font, new TranslatableComponent("gui.advancedmagnet.items"), 53, 26, 4210752);
        ScreenUtils.drawCenteredString(matrixStack, this.font, new TranslatableComponent("gui.advancedmagnet.xp"), 147, 26, 4210752);
        ScreenUtils.drawString(matrixStack, this.font, new TranslatableComponent("gui.advancedmagnet.filter"), 8, 68, 4210752);

        ScreenUtils.drawCenteredString(matrixStack, this.font, new TextComponent("" + this.itemRange), 68, 44, 4210752);
        ScreenUtils.drawCenteredString(matrixStack, this.font, new TextComponent("" + this.xpRange), 162, 44, 4210752);
    }

    @Override
    protected void renderTooltips(PoseStack matrixStack, int mouseX, int mouseY, ItemStack stack){
        if(mouseX > 68 - 5 && mouseX < 68 + 5 && mouseY > 44 - 5 && mouseY < 44 + 5)
            this.renderTooltip(matrixStack, new TranslatableComponent("gui.advancedmagnet.items.range", this.itemRange), mouseX, mouseY);

        if(mouseX > 162 - 5 && mouseX < 162 + 5 && mouseY > 44 - 5 && mouseY < 44 + 5)
            this.renderTooltip(matrixStack, new TranslatableComponent("gui.advancedmagnet.xp.range", this.xpRange), mouseX, mouseY);
    }

    public CompoundTag getTagOrClose(){
        ItemStack stack = this.getObjectOrClose();
        if(stack.getItem() instanceof AdvancedMagnet)
            return stack.getOrCreateTag();
        return null;
    }
}
