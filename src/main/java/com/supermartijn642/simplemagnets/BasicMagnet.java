package com.supermartijn642.simplemagnets;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class BasicMagnet extends MagnetItem {

    public static final int RANGE = 5;

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
        return RANGE;
    }

    @Override
    protected int getRangeXp(NBTTagCompound tag){
        return RANGE;
    }
}
