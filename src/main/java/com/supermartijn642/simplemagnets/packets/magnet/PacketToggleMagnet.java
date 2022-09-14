package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.simplemagnets.MagnetItem;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class PacketToggleMagnet implements BasePacket {

    @Override
    public void write(PacketBuffer buffer){
    }

    @Override
    public void read(PacketBuffer buffer){
    }

    @Override
    public void handle(PacketContext context){
        EntityPlayer player = context.getSendingPlayer();
        if(player != null){
            ItemStack stack = findStack(player);
            if(stack != null && !stack.isEmpty())
                MagnetItem.toggleMagnet(player, stack);
        }
    }

    private static ItemStack findStack(EntityPlayer player){
        ItemStack stack = findCuriosStack(player);
        if(stack != null && !stack.isEmpty() && stack.getItem() instanceof MagnetItem)
            return stack;

        for(int slot = 0; slot < player.inventory.getSizeInventory(); slot++){
            stack = player.inventory.getStackInSlot(slot);
            if(!stack.isEmpty() && stack.getItem() instanceof MagnetItem)
                return stack;
        }

        return null;
    }

    private static ItemStack findCuriosStack(EntityPlayer player){
        return SimpleMagnets.baubles.getMagnetStack(player);
    }
}
