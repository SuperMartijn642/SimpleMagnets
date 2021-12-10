package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.simplemagnets.AdvancedMagnet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class PacketToggleMagnetDurability implements BasePacket {

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
                stack.getOrCreateTag().putBoolean("filterDurability", !(stack.getOrCreateTag().contains("filterDurability") && stack.getOrCreateTag().getBoolean("filterDurability")));
                player.setItemInHand(Hand.MAIN_HAND, stack);
            }
        }
    }
}
