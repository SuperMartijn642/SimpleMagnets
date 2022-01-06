package com.supermartijn642.simplemagnets.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ItemBaseContainerScreen;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.simplemagnets.AdvancedMagnet;
import com.supermartijn642.simplemagnets.SMConfig;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import com.supermartijn642.simplemagnets.packets.magnet.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class MagnetContainerScreen extends ItemBaseContainerScreen<MagnetContainer> {

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

    public MagnetContainerScreen(MagnetContainer container, PlayerInventory inv, ITextComponent title){
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
        CompoundNBT tag = stack.getOrCreateTag();

        this.itemCheckbox = this.addWidget(new CheckBox(11, 38, checked -> checked ? "gui.advancedmagnet.items.on" : "gui.advancedmagnet.items.off", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleItems())));
        this.itemCheckbox.update(!(tag.contains("items") && tag.getBoolean("items")));

        this.leftItemButton = this.addWidget(new PlusMinusButton(54, 38, true, "gui.advancedmagnet.items.decrease", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseItemRange())));
        this.rightItemButton = this.addWidget(new PlusMinusButton(88, 38, false, "gui.advancedmagnet.items.increase", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseItemRange())));

        this.xpCheckbox = this.addWidget(new CheckBox(119, 38, checked -> checked ? "gui.advancedmagnet.xp.on" : "gui.advancedmagnet.xp.off", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleXp())));
        this.xpCheckbox.update(!(tag.contains("xp") && tag.getBoolean("xp")));

        this.leftXpButton = this.addWidget(new PlusMinusButton(162, 38, true, "gui.advancedmagnet.xp.decrease", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseXpRange())));
        this.rightXpButton = this.addWidget(new PlusMinusButton(196, 38, false, "gui.advancedmagnet.xp.increase", () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseXpRange())));

        this.whitelistButton = this.addWidget(new WhitelistButton(175, 78, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleMagnetWhitelist())));
        this.whitelistButton.update(tag.contains("whitelist") && tag.getBoolean("whitelist"));

        this.durabilityButton = this.addWidget(new DurabilityButton(197, 78, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleMagnetDurability())));
        this.durabilityButton.update(tag.contains("filterDurability") && tag.getBoolean("filterDurability"));
    }

    @Override
    public void tick(){
        CompoundNBT tag = this.getTagOrClose();
        if(tag == null)
            return;

        super.tick();

        this.itemRange = tag.contains("itemRange") ? tag.getInt("itemRange") : SMConfig.advancedMagnetRange.get();
        this.xpRange = tag.contains("xpRange") ? tag.getInt("xpRange") : SMConfig.advancedMagnetRange.get();

        boolean items = !(tag.contains("items") && tag.getBoolean("items"));
        this.itemCheckbox.update(items);
        this.leftItemButton.setActive(items && this.itemRange > SMConfig.advancedMagnetMinRange.get());
        this.rightItemButton.setActive(items && this.itemRange < SMConfig.advancedMagnetMaxRange.get());
        boolean experience = !(tag.contains("xp") && tag.getBoolean("xp"));
        this.xpCheckbox.update(experience);
        this.leftXpButton.setActive(experience && this.xpRange > SMConfig.advancedMagnetMinRange.get());
        this.rightXpButton.setActive(experience && this.xpRange < SMConfig.advancedMagnetMaxRange.get());
        this.whitelistButton.update(tag.contains("whitelist") && tag.getBoolean("whitelist"));
        this.durabilityButton.update(tag.contains("filterDurability") && tag.getBoolean("filterDurability"));
    }

    @Override
    protected void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY, ItemStack object){
        ScreenUtils.bindTexture(BACKGROUND);
        ScreenUtils.drawTexture(matrixStack, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderForeground(MatrixStack matrixStack, int mouseX, int mouseY, ItemStack object){
        ScreenUtils.drawCenteredString(matrixStack, this.font, this.title, this.imageWidth / 2f, 6, 4210752);
        ScreenUtils.drawString(matrixStack, this.font, this.inventory.getDisplayName(), 32, 102, 4210752);

        ScreenUtils.drawCenteredString(matrixStack, this.font, TextComponents.translation("gui.advancedmagnet.items").get(), 58, 24, 4210752);
        ScreenUtils.drawCenteredString(matrixStack, this.font, TextComponents.translation("gui.advancedmagnet.xp").get(), 166, 24, 4210752);
        ScreenUtils.drawCenteredString(matrixStack, this.font, TextComponents.translation("gui.advancedmagnet.filter").get(), 112, 68, 4210752);

        ScreenUtils.drawCenteredString(matrixStack, this.font, TextComponents.number(this.itemRange).get(), 79.5f, 43, 4210752);
        ScreenUtils.drawCenteredString(matrixStack, this.font, TextComponents.number(this.xpRange).get(), 187.5f, 43, 4210752);
    }

    @Override
    protected void renderTooltips(MatrixStack matrixStack, int mouseX, int mouseY, ItemStack stack){
        if(mouseX > 79.5f - 5 && mouseX < 79.5f + 5 && mouseY > 43 - 5 && mouseY < 43 + 5)
            this.renderTooltip(matrixStack, new TranslationTextComponent("gui.advancedmagnet.items.range", this.itemRange), mouseX, mouseY);

        if(mouseX > 187.5f - 5 && mouseX < 187.5f + 5 && mouseY > 43 - 5 && mouseY < 43 + 5)
            this.renderTooltip(matrixStack, new TranslationTextComponent("gui.advancedmagnet.xp.range", this.xpRange), mouseX, mouseY);
    }

    public CompoundNBT getTagOrClose(){
        ItemStack stack = this.getObjectOrClose();
        if(stack != null && stack.getItem() instanceof AdvancedMagnet)
            return stack.getOrCreateTag();
        return null;
    }
}
