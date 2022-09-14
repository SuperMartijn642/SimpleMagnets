package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.simplemagnets.gui.MagnetContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class AdvancedMagnet extends MagnetItem {

    @Override
    public ItemUseResult interact(ItemStack stack, EntityPlayer player, EnumHand hand, World level){
        if(!player.isSneaking())
            return super.interact(stack, player, hand, level);

        int slot = hand == EnumHand.MAIN_HAND ? player.inventory.currentItem : 40;
        if(!level.isRemote)
            CommonUtils.openContainer(new MagnetContainer(player, slot));
        return ItemUseResult.success(stack);
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
        boolean filterDurability = tag.hasKey("filterDurability") && tag.getBoolean("filterDurability");
        for(int slot = 0; slot < 9; slot++){
            if(tag.hasKey("filter" + slot)){
                ItemStack stack1 = new ItemStack(tag.getCompoundTag("filter" + slot));
                boolean equal = filterDurability ?
                    ItemStack.areItemsEqual(stack, stack1) && ItemStack.areItemStackTagsEqual(stack, stack1) :
                    ItemStack.areItemsEqualIgnoreDurability(stack, stack1);
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
        return tag != null && tag.hasKey("itemRange") ? tag.getInteger("itemRange") : SMConfig.advancedMagnetRange.get();
    }

    @Override
    protected int getRangeXp(NBTTagCompound tag){
        return tag != null && tag.hasKey("xpRange") ? tag.getInteger("xpRange") : SMConfig.advancedMagnetRange.get();
    }

    @Override
    protected ITextComponent getTooltip(){
        return TextComponents.translation("simplemagnets.advancedmagnet.info", TextComponents.number(SMConfig.advancedMagnetMaxRange.get()).color(TextFormatting.GOLD).get()).color(TextFormatting.GRAY).get();
    }

}
