package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.TextComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class BasicMagnet extends MagnetItem {

    @Override
    protected boolean canPickupItems(ItemStack magnet){
        return true;
    }

    @Override
    protected boolean canPickupStack(ItemStack magnet, ItemStack stack){
        return true;
    }

    @Override
    protected boolean canPickupXp(ItemStack magnet){
        return true;
    }

    @Override
    protected int getRangeItems(ItemStack magnet){
        return SMConfig.basicMagnetRange.get();
    }

    @Override
    protected int getRangeXp(ItemStack magnet){
        return SMConfig.basicMagnetRange.get();
    }

    @Override
    protected Component getTooltip(){
        return TextComponents.translation("simplemagnets.basicmagnet.info", TextComponents.number(SMConfig.basicMagnetRange.get()).color(ChatFormatting.GOLD).get()).color(ChatFormatting.GRAY).get();
    }
}
