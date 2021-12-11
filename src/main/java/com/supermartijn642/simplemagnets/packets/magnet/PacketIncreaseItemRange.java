package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.simplemagnets.AdvancedMagnet;
import com.supermartijn642.simplemagnets.SMConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumHand;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class PacketIncreaseItemRange implements BasePacket {

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
            ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
            if(stack.getItem() instanceof AdvancedMagnet){
                NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
                tag.setInteger("itemRange", Math.min(SMConfig.advancedMagnetMaxRange.get(), (tag.hasKey("itemRange") ? tag.getInteger("itemRange") : SMConfig.advancedMagnetRange.get()) + 1));
                stack.setTagCompound(tag);
                player.setHeldItem(EnumHand.MAIN_HAND, stack);
            }
        }
    }
}
