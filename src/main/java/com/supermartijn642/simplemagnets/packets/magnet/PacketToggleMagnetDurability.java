package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.simplemagnets.AdvancedMagnet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class PacketToggleMagnetDurability {

    public void encode(PacketBuffer buffer){
    }

    public static PacketToggleMagnetDurability decode(PacketBuffer buffer){
        return new PacketToggleMagnetDurability();
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier){
        contextSupplier.get().setPacketHandled(true);

        PlayerEntity player = contextSupplier.get().getSender();
        if(player != null){
            ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);

            if(stack.getItem() instanceof AdvancedMagnet)
                stack.getOrCreateTag().putBoolean("filterDurability", !(stack.getOrCreateTag().contains("filterDurability") && stack.getOrCreateTag().getBoolean("filterDurability")));
        }
    }
}
