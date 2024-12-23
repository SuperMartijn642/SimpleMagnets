package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.simplemagnets.MagnetItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class PacketToggleMagnet implements BasePacket {

    @Override
    public void write(FriendlyByteBuf buffer){
    }

    @Override
    public void read(FriendlyByteBuf buffer){
    }

    @Override
    public void handle(PacketContext context){
        Player player = context.getSendingPlayer();
        if(player != null){
            ItemStack stack = findStack(player);
            if(stack != null && !stack.isEmpty())
                MagnetItem.toggleMagnet(player, stack);
        }
    }

    private static ItemStack findStack(Player player){
        for(int slot = 0; slot < player.getInventory().getContainerSize(); slot++){
            ItemStack stack = player.getInventory().getItem(slot);
            if(!stack.isEmpty() && stack.getItem() instanceof MagnetItem)
                return stack;
        }

        return null;
    }
}
