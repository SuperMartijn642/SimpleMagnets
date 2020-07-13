package com.supermartijn642.simplemagnets.integration;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created 7/14/2020 by SuperMartijn642
 */
public class BaublesActive extends BaublesInactive {

    @Override
    public boolean isBaubleCapability(Capability<?> capability){
        return capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;
    }

    @Override
    public <T> T getBaubleCapability(Capability<T> capability, Item item){
        return BaublesCapabilities.CAPABILITY_ITEM_BAUBLE.cast(new IBauble() {
            @Override
            public BaubleType getBaubleType(ItemStack itemStack){
                return BaubleType.CHARM;
            }

            @Override
            public void onWornTick(ItemStack itemstack, EntityLivingBase player){
                item.onUpdate(itemstack, player.world, player, -1, false);
            }
        });
    }
}
