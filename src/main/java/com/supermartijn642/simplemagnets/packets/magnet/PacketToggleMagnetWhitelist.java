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
public class PacketToggleMagnetWhitelist implements BasePacket {

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
                stack.getOrCreateTag().putBoolean("whitelist", !(stack.getOrCreateTag().contains("whitelist") && stack.getOrCreateTag().getBoolean("whitelist")));
                player.setItemInHand(Hand.MAIN_HAND, stack);
            }
        }
    }
}
