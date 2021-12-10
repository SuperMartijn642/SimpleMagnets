package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.simplemagnets.AdvancedMagnet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class PacketToggleMagnetWhitelist implements BasePacket {

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
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(stack.getItem() instanceof AdvancedMagnet){
                stack.getOrCreateTag().putBoolean("whitelist", !(stack.getOrCreateTag().contains("whitelist") && stack.getOrCreateTag().getBoolean("whitelist")));
                player.setItemInHand(InteractionHand.MAIN_HAND, stack);
            }
        }
    }
}
