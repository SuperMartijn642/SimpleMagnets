package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.simplemagnets.MagnetItem;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class PacketToggleMagnet implements IMessage, IMessageHandler<PacketToggleMagnet,IMessage> {

    @Override
    public void fromBytes(ByteBuf buffer){
    }

    @Override
    public void toBytes(ByteBuf buffer){
    }

    @Override
    public IMessage onMessage(PacketToggleMagnet message, MessageContext ctx){
        EntityPlayer player = ctx.getServerHandler().player;
        if(player != null){
            ItemStack stack = findStack(player);
            if(stack != null && !stack.isEmpty())
                MagnetItem.toggleMagnet(player, stack);
        }
        return null;
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
