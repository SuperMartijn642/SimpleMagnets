package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.simplemagnets.AdvancedMagnet;
import com.supermartijn642.simplemagnets.SMConfig;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import com.supermartijn642.simplemagnets.packets.magnet.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class MagnetContainerScreen extends GuiContainer {

    private final ResourceLocation BACKGROUND = new ResourceLocation("simplemagnets", "textures/screen.png");

    private final MagnetContainer container;
    private final InventoryPlayer playerInventory;

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

    public MagnetContainerScreen(MagnetContainer container, InventoryPlayer inv){
        super(container);
        this.container = container;
        this.playerInventory = inv;

        this.xSize = 224;
        this.ySize = 196;
    }

    @Override
    public void initGui(){
        NBTTagCompound tag = this.getTagOrClose();
        if(tag == null)
            return;

        super.initGui();

        this.itemCheckbox = this.addButton(new CheckBox(0, this.guiLeft + 19, this.guiTop + 39, () -> SimpleMagnets.channel.sendToServer(new PacketToggleItems())));
        this.itemCheckbox.update(!(tag.hasKey("items") && tag.getBoolean("items")));

        this.leftItemButton = this.addButton(new LeftRightArrowButton(1, this.guiLeft + 45, this.guiTop + 39, true, () -> SimpleMagnets.channel.sendToServer(new PacketDecreaseItemRange())));

        this.rightItemButton = this.addButton(new LeftRightArrowButton(2, this.guiLeft + 78, this.guiTop + 39, false, () -> SimpleMagnets.channel.sendToServer(new PacketIncreaseItemRange())));

        this.xpCheckbox = this.addButton(new CheckBox(3, this.guiLeft + 113, this.guiTop + 39, () -> SimpleMagnets.channel.sendToServer(new PacketToggleXp())));
        this.xpCheckbox.update(!(tag.hasKey("xp") && tag.getBoolean("xp")));

        this.leftXpButton = this.addButton(new LeftRightArrowButton(4, this.guiLeft + 139, this.guiTop + 39, true, () -> SimpleMagnets.channel.sendToServer(new PacketDecreaseXpRange())));

        this.rightXpButton = this.addButton(new LeftRightArrowButton(5, this.guiLeft + 172, this.guiTop + 39, false, () -> SimpleMagnets.channel.sendToServer(new PacketIncreaseXpRange())));

        this.whitelistButton = this.addButton(new WhitelistButton(6, this.guiLeft + 175, this.guiTop + 78, () -> SimpleMagnets.channel.sendToServer(new PacketToggleMagnetWhitelist())));
        this.whitelistButton.update(tag.hasKey("whitelist") && tag.getBoolean("whitelist"));

        this.durabilityButton = this.addButton(new DurabilityButton(7, this.guiLeft + 197, this.guiTop + 78, () -> SimpleMagnets.channel.sendToServer(new PacketToggleMagnetDurability())));
        this.durabilityButton.update(tag.hasKey("filterDurability") && tag.getBoolean("filterDurability"));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

        if(this.itemCheckbox.isMouseOver())
            this.renderTooltip(true, this.itemCheckbox.checked ? "gui.advancedmagnet.items.on" : "gui.advancedmagnet.items.off", mouseX, mouseY);
        if(this.leftItemButton.isMouseOver())
            this.renderTooltip(true, "gui.advancedmagnet.items.decrease", mouseX, mouseY);
        if(this.rightItemButton.isMouseOver())
            this.renderTooltip(true, "gui.advancedmagnet.items.increase", mouseX, mouseY);
        if(mouseX > this.guiLeft + 68 - 5 && mouseX < this.guiLeft + 68 + 5 && mouseY > this.guiTop + 44 - 5 && mouseY < this.guiTop + 44 + 5)
            this.renderTooltip(false, new TextComponentTranslation("gui.advancedmagnet.items.range").getFormattedText().replace("$number$", "" + this.itemRange), mouseX, mouseY);

        if(this.xpCheckbox.isMouseOver())
            this.renderTooltip(true, this.xpCheckbox.checked ? "gui.advancedmagnet.xp.on" : "gui.advancedmagnet.xp.off", mouseX, mouseY);
        if(this.leftXpButton.isMouseOver())
            this.renderTooltip(true, "gui.advancedmagnet.xp.decrease", mouseX, mouseY);
        if(this.rightXpButton.isMouseOver())
            this.renderTooltip(true, "gui.advancedmagnet.xp.increase", mouseX, mouseY);
        if(mouseX > this.guiLeft + 162 - 5 && mouseX < this.guiLeft + 162 + 5 && mouseY > this.guiTop + 44 - 5 && mouseY < this.guiTop + 44 + 5)
            this.renderTooltip(false, new TextComponentTranslation("gui.advancedmagnet.xp.range").getFormattedText().replace("$number$", "" + this.xpRange), mouseX, mouseY);

        if(this.whitelistButton.isMouseOver())
            this.renderTooltip(true, this.whitelistButton.white ? "gui.advancedmagnet.whitelist.on" : "gui.advancedmagnet.whitelist.off", mouseX, mouseY);
        if(this.durabilityButton.isMouseOver())
            this.renderTooltip(true, "gui.simplemagnets.demagnetization_coil.durability." + (this.durabilityButton.on ? "on" : "off"), mouseX, mouseY);
    }

    @Override
    public void updateScreen(){
        NBTTagCompound tag = this.getTagOrClose();
        if(tag == null)
            return;

        super.updateScreen();

        this.itemCheckbox.update(!(tag.hasKey("items") && tag.getBoolean("items")));
        this.xpCheckbox.update(!(tag.hasKey("xp") && tag.getBoolean("xp")));
        this.whitelistButton.update(tag.hasKey("whitelist") && tag.getBoolean("whitelist"));
        this.durabilityButton.update(tag.hasKey("filterDurability") && tag.getBoolean("filterDurability"));

        this.itemRange = tag.hasKey("itemRange") ? tag.getInteger("itemRange") : SMConfig.advancedMagnetRange.get();
        this.xpRange = tag.hasKey("xpRange") ? tag.getInteger("xpRange") : SMConfig.advancedMagnetRange.get();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        this.drawCenteredString(new TextComponentTranslation("gui.advancedmagnet.title"), this.xSize / 2, 6);
        this.drawString(this.playerInventory.getDisplayName(), 32, 102);

        this.drawCenteredString(new TextComponentTranslation("gui.advancedmagnet.items"), 53, 26);
        this.drawCenteredString(new TextComponentTranslation("gui.advancedmagnet.xp"), 147, 26);
        this.drawString(new TextComponentTranslation("gui.advancedmagnet.filter"), 8, 68);

        this.drawCenteredString(new TextComponentTranslation("" + this.itemRange), 68, 44);
        this.drawCenteredString(new TextComponentTranslation("" + this.xpRange), 162, 44);
    }

    public NBTTagCompound getTagOrClose(){
        ItemStack stack = this.container.player.inventory.getStackInSlot(this.container.slot);
        if(stack.getItem() instanceof AdvancedMagnet){
            NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
            stack.setTagCompound(tag);
            return tag;
        }
        this.container.player.closeScreen();
        return null;
    }

    public void drawCenteredString(ITextComponent text, int x, int y){
        String s = text.getFormattedText();
        this.fontRenderer.drawString(s, this.guiLeft + x - this.fontRenderer.getStringWidth(s) / 2, this.guiTop + y, 4210752);
    }

    public void drawString(ITextComponent text, int x, int y){
        String s = text.getFormattedText();
        this.fontRenderer.drawString(s, this.guiLeft + x, this.guiTop + y, 4210752);
    }

    public void renderTooltip(boolean translate, String string, int x, int y){
        super.drawHoveringText(translate ? new TextComponentTranslation(string).getFormattedText() : string, x, y);
    }

    @Override
    protected void actionPerformed(GuiButton button){
        if(button instanceof Pressable)
            ((Pressable)button).onPress();
    }
}
