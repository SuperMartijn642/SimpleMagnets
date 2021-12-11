package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.TextComponents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class BasicMagnet extends MagnetItem {


    public BasicMagnet(){
        super("basicmagnet");
    }

    @Override
    protected boolean canPickupItems(NBTTagCompound tag){
        return true;
    }

    @Override
    protected boolean canPickupStack(NBTTagCompound tag, ItemStack stack){
        return true;
    }

    @Override
    protected boolean canPickupXp(NBTTagCompound tag){
        return true;
    }

    @Override
    protected int getRangeItems(NBTTagCompound tag){
        return SMConfig.basicMagnetRange.get();
    }

    @Override
    protected int getRangeXp(NBTTagCompound tag){
        return SMConfig.basicMagnetRange.get();
    }

    @Override
    protected ITextComponent getTooltip(){
        return TextComponents.translation("simplemagnets.basicmagnet.info", TextComponents.number(SMConfig.basicMagnetRange.get()).color(TextFormatting.GOLD).get()).color(TextFormatting.GRAY).get();
    }
}
