package com.supermartijn642.simplemagnets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class BasicMagnet extends MagnetItem {


    public BasicMagnet(){
        super("basicmagnet");
    }

    @Override
    protected boolean canPickupItems(CompoundTag tag){
        return true;
    }

    @Override
    protected boolean canPickupStack(CompoundTag tag, ItemStack stack){
        return true;
    }

    @Override
    protected boolean canPickupXp(CompoundTag tag){
        return true;
    }

    @Override
    protected int getRangeItems(CompoundTag tag){
        return SMConfig.basicMagnetRange.get();
    }

    @Override
    protected int getRangeXp(CompoundTag tag){
        return SMConfig.basicMagnetRange.get();
    }

    @Override
    protected BaseComponent getTooltip(){
        return new TranslatableComponent("simplemagnets.basicmagnet.info", SMConfig.basicMagnetRange.get());
    }
}
