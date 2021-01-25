package com.supermartijn642.simplemagnets.packets;

import com.supermartijn642.simplemagnets.AdvancedMagnet;
import com.supermartijn642.simplemagnets.SMConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class PacketIncreaseXpRange implements IMessage, IMessageHandler<PacketIncreaseXpRange,IMessage> {

    @Override
    public void fromBytes(ByteBuf buffer){
    }

    @Override
    public void toBytes(ByteBuf buffer){
    }

    @Override
    public IMessage onMessage(PacketIncreaseXpRange message, MessageContext ctx){
        EntityPlayer player = ctx.getServerHandler().player;
        if(player != null){
            ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);

            if(stack.getItem() instanceof AdvancedMagnet){
                NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
                tag.setInteger("xpRange", Math.min(SMConfig.advancedMagnetMaxRange.get(), (tag.hasKey("xpRange") ? tag.getInteger("xpRange") : SMConfig.advancedMagnetRange.get()) + 1));
                stack.setTagCompound(tag);
            }
        }
        return null;
    }
}
