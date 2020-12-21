package com.supermartijn642.simplemagnets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class AdvancedMagnet extends MagnetItem {

    public static final int DEFAULT_RANGE = 8;
    public static final int MIN_RANGE = 3;
    public static final int MAX_RANGE = 11;

    public AdvancedMagnet(){
        super("advancedmagnet");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
        if(!playerIn.isSneaking())
            return super.onItemRightClick(worldIn, playerIn, handIn);
        int slot = handIn == EnumHand.MAIN_HAND ? playerIn.inventory.currentItem : 40;
        if(!worldIn.isRemote)
            playerIn.openGui(SimpleMagnets.instance, slot, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    protected boolean canPickupItems(NBTTagCompound tag){
        return !(tag != null && tag.hasKey("items") && tag.getBoolean("items"));
    }

    @Override
    protected boolean canPickupStack(NBTTagCompound tag, ItemStack stack){
        if(tag == null)
            return true;
        boolean whitelist = tag.hasKey("whitelist") && tag.getBoolean("whitelist");
        for(int slot = 0; slot < 9; slot++){
            if(tag.hasKey("filter" + slot)){
                ItemStack stack1 = new ItemStack(tag.getCompoundTag("filter" + slot));
                boolean equal = ItemStack.areItemsEqual(stack, stack1) && ItemStack.areItemStackTagsEqual(stack, stack1);
                if(equal)
                    return whitelist;
            }
        }
        return !whitelist;
    }

    @Override
    protected boolean canPickupXp(NBTTagCompound tag){
        return !(tag != null && tag.hasKey("xp") && tag.getBoolean("xp"));
    }

    @Override
    protected int getRangeItems(NBTTagCompound tag){
        return tag != null && tag.hasKey("itemRange") ? tag.getInteger("itemRange") : DEFAULT_RANGE;
    }

    @Override
    protected int getRangeXp(NBTTagCompound tag){
        return tag != null && tag.hasKey("xpRange") ? tag.getInteger("xpRange") : DEFAULT_RANGE;
    }

    @Override
    protected ITextComponent getTooltip(){
        return new TextComponentTranslation("simplemagnets.advancedmagnet.info", MAX_RANGE);
    }

}
