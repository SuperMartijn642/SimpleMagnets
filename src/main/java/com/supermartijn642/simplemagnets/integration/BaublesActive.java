package com.supermartijn642.simplemagnets.integration;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
import com.supermartijn642.simplemagnets.MagnetItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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

    @Override
    public ItemStack getMagnetStack(EntityPlayer player){
        IBaublesItemHandler handler = player.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null);
        if(handler != null){
            for(int slot = 0; slot < handler.getSlots(); slot++){
                ItemStack stack = handler.getStackInSlot(slot);
                if(stack.getItem() instanceof MagnetItem)
                    return stack;
            }
        }
        return null;
    }
}
