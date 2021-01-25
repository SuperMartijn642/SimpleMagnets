package com.supermartijn642.simplemagnets.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.supermartijn642.simplemagnets.AdvancedMagnet;
import com.supermartijn642.simplemagnets.SMConfig;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import com.supermartijn642.simplemagnets.packets.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class MagnetContainerScreen extends ContainerScreen<MagnetContainer> {

    private final ResourceLocation BACKGROUND = new ResourceLocation("simplemagnets", "textures/screen.png");

    private CheckBox itemCheckbox;
    private ArrowButton leftItemButton;
    private ArrowButton rightItemButton;
    private CheckBox xpCheckbox;
    private ArrowButton leftXpButton;
    private ArrowButton rightXpButton;
    private WhitelistButton whitelistButton;

    private int itemRange = SMConfig.advancedMagnetRange.get();
    private int xpRange = SMConfig.advancedMagnetRange.get();

    public MagnetContainerScreen(MagnetContainer container, PlayerInventory inv, ITextComponent title){
        super(container, inv, title);
        this.xSize = 202;
        this.ySize = 196;
    }

    @Override
    protected void init(){
        CompoundNBT tag = this.getTagOrClose();
        if(tag == null)
            return;

        super.init();

        this.itemCheckbox = this.addButton(new CheckBox(this.guiLeft + 19, this.guiTop + 39, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleItems())));
        this.itemCheckbox.update(!(tag.contains("items") && tag.getBoolean("items")));

        this.leftItemButton = this.addButton(new ArrowButton(this.guiLeft + 45, this.guiTop + 39, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseItemRange())));

        this.rightItemButton = this.addButton(new ArrowButton(this.guiLeft + 78, this.guiTop + 39, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseItemRange())));

        this.xpCheckbox = this.addButton(new CheckBox(this.guiLeft + 113, this.guiTop + 39, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleXp())));
        this.xpCheckbox.update(!(tag.contains("xp") && tag.getBoolean("xp")));

        this.leftXpButton = this.addButton(new ArrowButton(this.guiLeft + 139, this.guiTop + 39, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseXpRange())));

        this.rightXpButton = this.addButton(new ArrowButton(this.guiLeft + 172, this.guiTop + 39, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseXpRange())));

        this.whitelistButton = this.addButton(new WhitelistButton(this.guiLeft + 175, this.guiTop + 78, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleWhitelist())));
        this.whitelistButton.update(tag.contains("whitelist") && tag.getBoolean("whitelist"));
    }

    public void render(int mouseX, int mouseY, float partialTicks){
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

        if(this.itemCheckbox.isHovered())
            this.renderTooltip(true, this.itemCheckbox.checked ? "gui.advancedmagnet.items.on" : "gui.advancedmagnet.items.off", mouseX, mouseY);
        if(this.leftItemButton.isHovered())
            this.renderTooltip(true, "gui.advancedmagnet.items.decrease", mouseX, mouseY);
        if(this.rightItemButton.isHovered())
            this.renderTooltip(true, "gui.advancedmagnet.items.increase", mouseX, mouseY);
        if(mouseX > this.guiLeft + 68 - 5 && mouseX < this.guiLeft + 68 + 5 && mouseY > this.guiTop + 44 - 5 && mouseY < this.guiTop + 44 + 5)
            this.renderTooltip(false, new TranslationTextComponent("gui.advancedmagnet.items.range").getFormattedText().replace("$number$", "" + this.itemRange), mouseX, mouseY);

        if(this.xpCheckbox.isHovered())
            this.renderTooltip(true, this.xpCheckbox.checked ? "gui.advancedmagnet.xp.on" : "gui.advancedmagnet.xp.off", mouseX, mouseY);
        if(this.leftXpButton.isHovered())
            this.renderTooltip(true, "gui.advancedmagnet.xp.decrease", mouseX, mouseY);
        if(this.rightXpButton.isHovered())
            this.renderTooltip(true, "gui.advancedmagnet.xp.increase", mouseX, mouseY);
        if(mouseX > this.guiLeft + 162 - 5 && mouseX < this.guiLeft + 162 + 5 && mouseY > this.guiTop + 44 - 5 && mouseY < this.guiTop + 44 + 5)
            this.renderTooltip(false, new TranslationTextComponent("gui.advancedmagnet.xp.range").getFormattedText().replace("$number$", "" + this.xpRange), mouseX, mouseY);

        if(this.whitelistButton.isHovered())
            this.renderTooltip(true, this.whitelistButton.white ? "gui.advancedmagnet.whitelist.on" : "gui.advancedmagnet.whitelist.off", mouseX, mouseY);
    }

    @Override
    public void tick(){
        CompoundNBT tag = this.getTagOrClose();
        if(tag == null)
            return;

        super.tick();

        this.itemCheckbox.update(!(tag.contains("items") && tag.getBoolean("items")));
        this.xpCheckbox.update(!(tag.contains("xp") && tag.getBoolean("xp")));
        this.whitelistButton.update(tag.contains("whitelist") && tag.getBoolean("whitelist"));

        this.itemRange = tag.contains("itemRange") ? tag.getInt("itemRange") : SMConfig.advancedMagnetRange.get();
        this.xpRange = tag.contains("xpRange") ? tag.getInt("xpRange") : SMConfig.advancedMagnetRange.get();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND);
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        this.drawCenteredString(this.title, this.xSize / 2f, 6);
        this.drawString(this.playerInventory.getDisplayName(), 21, 102);

        this.drawCenteredString(new TranslationTextComponent("gui.advancedmagnet.items"), 53, 26);
        this.drawCenteredString(new TranslationTextComponent("gui.advancedmagnet.xp"), 147, 26);
        this.drawString(new TranslationTextComponent("gui.advancedmagnet.filter"), 8, 68);

        this.drawCenteredString(new StringTextComponent("" + this.itemRange), 68, 44);
        this.drawCenteredString(new StringTextComponent("" + this.xpRange), 162, 44);
    }

    public CompoundNBT getTagOrClose(){
        ItemStack stack = this.container.player.inventory.getStackInSlot(this.container.slot);
        if(stack.getItem() instanceof AdvancedMagnet)
            return stack.getOrCreateTag();
        this.container.player.closeScreen();
        return null;
    }

    public void drawCenteredString(ITextComponent text, float x, float y){
        String s = text.getFormattedText();
        this.font.drawString(s, this.guiLeft + x - this.font.getStringWidth(s) / 2f, this.guiTop + y, 4210752);
    }

    public void drawString(ITextComponent text, float x, float y){
        String s = text.getFormattedText();
        this.font.drawString(s, this.guiLeft + x, this.guiTop + y, 4210752);
    }

    public void renderTooltip(boolean translate, String string, int x, int y){
        super.renderTooltip(translate ? new TranslationTextComponent(string).getFormattedText() : string, x, y);
    }
}
