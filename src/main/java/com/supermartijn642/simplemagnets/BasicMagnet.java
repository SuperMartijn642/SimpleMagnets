package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.TextComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
    protected Component getTooltip(){
        return TextComponents.translation("simplemagnets.basicmagnet.info", SMConfig.basicMagnetRange.get()).get();
    }
}
