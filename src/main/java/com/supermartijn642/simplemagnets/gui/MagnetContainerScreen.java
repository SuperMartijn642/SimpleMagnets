package com.supermartijn642.simplemagnets.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.supermartijn642.simplemagnets.AdvancedMagnet;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import com.supermartijn642.simplemagnets.packets.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
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

    private int itemRange = AdvancedMagnet.DEFAULT_RANGE;
    private int xpRange = AdvancedMagnet.DEFAULT_RANGE;

    public MagnetContainerScreen(MagnetContainer container, PlayerInventory inv, ITextComponent title){
        super(container, inv, title);
        this.xSize = 202;
        this.ySize = 196;
    }

    @Override
    protected void func_231160_c_(){
        CompoundNBT tag = this.getTagOrClose();
        if(tag == null)
            return;

        super.func_231160_c_();

        this.itemCheckbox = this.func_230480_a_(new CheckBox(this.guiLeft + 19, this.guiTop + 39, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleItems())));
        this.itemCheckbox.update(!(tag.contains("items") && tag.getBoolean("items")));

        this.leftItemButton = this.func_230480_a_(new ArrowButton(this.guiLeft + 45, this.guiTop + 39, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseItemRange())));

        this.rightItemButton = this.func_230480_a_(new ArrowButton(this.guiLeft + 78, this.guiTop + 39, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseItemRange())));

        this.xpCheckbox = this.func_230480_a_(new CheckBox(this.guiLeft + 113, this.guiTop + 39, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleXp())));
        this.xpCheckbox.update(!(tag.contains("xp") && tag.getBoolean("xp")));

        this.leftXpButton = this.func_230480_a_(new ArrowButton(this.guiLeft + 139, this.guiTop + 39, true, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketDecreaseXpRange())));

        this.rightXpButton = this.func_230480_a_(new ArrowButton(this.guiLeft + 172, this.guiTop + 39, false, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketIncreaseXpRange())));

        this.whitelistButton = this.func_230480_a_(new WhitelistButton(this.guiLeft + 175, this.guiTop + 78, () -> SimpleMagnets.CHANNEL.sendToServer(new PacketToggleWhitelist())));
        this.whitelistButton.update(tag.contains("whitelist") && tag.getBoolean("whitelist"));
    }

    public void func_230430_a_(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks){
        this.func_230446_a_(matrixStack);
        super.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrixStack, mouseX, mouseY);

        if(this.itemCheckbox.func_230449_g_())
            this.renderTooltip(matrixStack, true, this.itemCheckbox.checked ? "gui.advancedmagnet.items.on" : "gui.advancedmagnet.items.off", mouseX, mouseY);
        if(this.leftItemButton.func_230449_g_())
            this.renderTooltip(matrixStack, true, "gui.advancedmagnet.items.decrease", mouseX, mouseY);
        if(this.rightItemButton.func_230449_g_())
            this.renderTooltip(matrixStack, true, "gui.advancedmagnet.items.increase", mouseX, mouseY);
        if(mouseX > this.guiLeft + 68 - 5 && mouseX < this.guiLeft + 68 + 5 && mouseY > this.guiTop + 44 - 5 && mouseY < this.guiTop + 44 + 5)
            this.renderTooltip(matrixStack, false, I18n.format("gui.advancedmagnet.items.range").replace("$number$","" + this.itemRange), mouseX, mouseY);

        if(this.xpCheckbox.func_230449_g_())
            this.renderTooltip(matrixStack, true, this.xpCheckbox.checked ? "gui.advancedmagnet.xp.on" : "gui.advancedmagnet.xp.off", mouseX, mouseY);
        if(this.leftXpButton.func_230449_g_())
            this.renderTooltip(matrixStack, true, "gui.advancedmagnet.xp.decrease", mouseX, mouseY);
        if(this.rightXpButton.func_230449_g_())
            this.renderTooltip(matrixStack, true, "gui.advancedmagnet.xp.increase", mouseX, mouseY);
        if(mouseX > this.guiLeft + 162 - 5 && mouseX < this.guiLeft + 162 + 5 && mouseY > this.guiTop + 44 - 5 && mouseY < this.guiTop + 44 + 5)
            this.renderTooltip(matrixStack, false, I18n.format("gui.advancedmagnet.xp.range").replace("$number$","" + this.xpRange), mouseX, mouseY);

        if(this.whitelistButton.func_230449_g_())
            this.renderTooltip(matrixStack, true, this.whitelistButton.white ? "gui.advancedmagnet.whitelist.on" : "gui.advancedmagnet.whitelist.off", mouseX, mouseY);
    }

    @Override
    public void func_231023_e_(){
        CompoundNBT tag = this.getTagOrClose();
        if(tag == null)
            return;

        super.func_231023_e_();

        this.itemCheckbox.update(!(tag.contains("items") && tag.getBoolean("items")));
        this.xpCheckbox.update(!(tag.contains("xp") && tag.getBoolean("xp")));
        this.whitelistButton.update(tag.contains("whitelist") && tag.getBoolean("whitelist"));

        this.itemRange = tag.contains("itemRange") ? tag.getInt("itemRange") : AdvancedMagnet.DEFAULT_RANGE;
        this.xpRange = tag.contains("xpRange") ? tag.getInt("xpRange") : AdvancedMagnet.DEFAULT_RANGE;
    }

    @Override
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY){
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND);
        this.func_238474_b_(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        this.drawCenteredString(matrixStack, this.field_230704_d_, this.xSize / 2f, 6);
        this.drawString(matrixStack, this.playerInventory.getDisplayName(), 21, 102);

        this.drawCenteredString(matrixStack, new TranslationTextComponent("gui.advancedmagnet.items"),53,26);
        this.drawCenteredString(matrixStack, new TranslationTextComponent("gui.advancedmagnet.xp"),147,26);
        this.drawString(matrixStack, new TranslationTextComponent("gui.advancedmagnet.filter"),8,68);

        this.drawCenteredString(matrixStack, new StringTextComponent("" + this.itemRange),68,44);
        this.drawCenteredString(matrixStack, new StringTextComponent("" + this.xpRange),162,44);
    }

    public CompoundNBT getTagOrClose(){
        ItemStack stack = this.container.player.inventory.getStackInSlot(this.container.slot);
        if(stack.getItem() instanceof AdvancedMagnet)
            return stack.getOrCreateTag();
        this.container.player.closeScreen();
        return null;
    }

    public void drawCenteredString(MatrixStack matrixStack, ITextComponent text, float x, float y){
        this.field_230712_o_.func_238422_b_(matrixStack, text, this.guiLeft + x - this.field_230712_o_.func_238414_a_(text) / 2f, this.guiTop + y, 4210752);
    }

    public void drawString(MatrixStack matrixStack, ITextComponent text, float x, float y){
        this.field_230712_o_.func_238422_b_(matrixStack, text, this.guiLeft + x, this.guiTop + y, 4210752);
    }

    public void renderTooltip(MatrixStack matrixStack, boolean translate, String string, int x, int y){
        super.func_238652_a_(matrixStack, translate ? new TranslationTextComponent(string) : new StringTextComponent(string), x, y);
    }

    @Override
    protected void func_230451_b_(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_){
        // stop default text drawing
    }
}
