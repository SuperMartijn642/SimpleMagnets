package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.simplemagnets.AdvancedMagnet;
import com.supermartijn642.simplemagnets.SMConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class PacketDecreaseXpRange implements BasePacket {

    @Override
    public void write(PacketBuffer buffer){
    }

    @Override
    public void read(PacketBuffer buffer){
    }

    @Override
    public void handle(PacketContext context){
        PlayerEntity player = context.getSendingPlayer();
        if(player != null){
            ItemStack stack = player.getItemInHand(Hand.MAIN_HAND);
            if(stack.getItem() instanceof AdvancedMagnet){
                stack.getOrCreateTag().putInt("xpRange", Math.max(SMConfig.advancedMagnetMinRange.get(), (stack.getOrCreateTag().contains("xpRange") ? stack.getOrCreateTag().getInt("xpRange") : SMConfig.advancedMagnetRange.get()) - 1));
                player.setItemInHand(Hand.MAIN_HAND, stack);
            }
        }
    }
}
